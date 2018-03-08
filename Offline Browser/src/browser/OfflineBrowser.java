package browser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class OfflineBrowser {
	private final Downloader input;
	private Map<String, Integer> elements = new HashMap();
	private Map<String, Integer> pages = new HashMap();
	private final Deque<Triple> deque = new ArrayDeque<>();
	private final String[] expansion = { ".css", ".js", ".png", ".jpeg", ".ico", ".html", ".vector", ".php" };
	private int number = 1;
	private int depth;
	private String url;
	private String dir;
	private String site = "";

	OfflineBrowser(Downloader d, String dir) {
		input = d;
		this.dir = dir;
	}

	public String merge(String url, String s) {
		if (s.lastIndexOf("#") != -1) {
			return merge(url, s.substring(0, s.lastIndexOf("#")));
		}
		try {
			return (new URL(new URL(url), s)).toString();
		} catch (MalformedURLException e) {
			return "";
		}
	}

	private void site(String url2) {
		if (url2.length() < 12) {
			return;
		}
		String str = url2.substring(12, url2.length());
		if (str.indexOf('.') == -1) {
			site = url2;
		} else {
			site = url2.substring(0, 12) + str.substring(0, str.indexOf('.'));
		}
		System.out.println("home site = " + site);
	}

	public void save(String url1, int depth1, boolean end) {
		if (end) {
			site(url1);
		}
		Triple trip = new Triple(url1, depth1);
		deque.addLast(trip);
		elements.put(url1, 1);
		while (!deque.isEmpty()) {
			this.url = deque.peekFirst().getUrl();
			if (!site.equals("") && !url.startsWith(site)) {
				System.out.print("scip: " + url);
				deque.removeFirst();
				continue;
			}
			System.out.println("parsing: " + url);
			if (pages.containsKey(url)) {
				deque.removeFirst();
				continue;
			}
			this.depth = deque.removeFirst().getDepth();
			if (depth == 0) {
				pages.put(url, elements.get(url));
				continue;
			}
			try (BufferedReader buff = new BufferedReader(new InputStreamReader(input.download(url), "UTF8"));
					PrintWriter prw = new PrintWriter(dir + "/Html" + elements.get(url) + ".html")) {
				Parser pars = new Parser(buff, prw);
				pars.parse();
				pages.put(url, elements.get(url));
			} catch (UnsupportedEncodingException e) {
			} catch (IOException e) {
			}
		}
	}

	private boolean save(String urlFile, String file) {
		try (InputStream buf = input.download(urlFile)) {
			try (OutputStream pw = new FileOutputStream(dir + "/" + file)) {
				byte[] buffer = new byte[1 << 16];
				int r;
				while ((r = buf.read(buffer)) != -1) {
					for (int i = 0; i < r; i++) {
						pw.write(buffer[i]);
					}
				}
				return true;
			} catch (FileNotFoundException e) {
				System.out.println("Error: File '" + file + "' can not be created or changed");
			} catch (SecurityException e) {
				System.out.println(
						"Error: File '" + file + "' is forbidden to be created or changed by security manager");
			} catch (IOException e) {
				System.out.println("Error while working with file " + e.getMessage());
			}
		} catch (IOException e) {
			// System.out.println("Error while working with site " + e.getMessage());
		}
		return false;
	}

	public class Parser {
		private boolean tit = true;
		private int ch;
		private BufferedReader buff;
		private PrintWriter pw;

		Parser(BufferedReader in, PrintWriter pw) {
			buff = in;
			this.pw = pw;
		}

		public void parse() throws IOException {
			skipWhitespace();
			while (ch != -1) {
				if (check('<')) {
					if (check('!')) {
						if (check('D')) {
							nextChar(true);
						} else {
							skipComment();
						}
					} else if (check('/')) {
						close();
					} else if (Character.isLetter(ch)) {
						open();
					} else {
						nextChar(true);
					}
				} else {
					nextChar(true);
				}
			}
		}

		private void nextChar(boolean write) {
			try {
				if (write) {
					pw.print((char) ch);
				}
				ch = buff.read();
			} catch (IOException e) {
				System.out.println("Error while working with site " + e.getMessage());
			}
		}

		private boolean check(char c) {
			if (ch == c) {
				nextChar(true);
				return true;
			}
			return false;
		}

		private boolean check1(char c) {
			if (ch == c) {
				nextChar(false);
				return true;
			}
			return false;
		}

		private String add(char c, boolean write) {
			StringBuilder str = new StringBuilder();
			while (ch != c) {
				str.append((char) ch);
				if (write) {
					nextChar(true);
				} else {
					nextChar(false);
				}
			}
			return str.toString();
		}

		private void skipWhitespace() {
			while (Character.isWhitespace(ch)) {
				nextChar(true);
			}
		}

		private void skipComment() {
			while (!(check('-') && check('-') && check('>'))) {
				nextChar(true);
			}
		}

		private void title() {
			StringBuilder str = new StringBuilder();
			while (true) {
				if (check('<')) {
					if (check('/')) {
						break;
					} else {
						str.append('<');
					}
				} else {
					str.append((char) ch);
					nextChar(true);
				}
			}
			close();
		}

		private String name() {
			StringBuilder sb = new StringBuilder();
			while (Character.isLetter(ch) || ch == '-' || ch == '_' || Character.isDigit(ch) || ch == ':'
					|| ch == '.') {
				sb.append((char) ch);
				nextChar(true);
			}
			return sb.toString();
		}

		private String change(String str) {
			return str.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&")
					.replaceAll("&mdash;", "\u2014").replaceAll("&nbsp;", "\u00A0");
		}

		private void open() throws IOException {
			String tag = name();
			skipWhitespace();
			while (!check('>') && !(check('/') && check('>'))) {
				String name = name();
				skipWhitespace();
				nextChar(true);
				skipWhitespace();
				String value = "";
				if (check1('"')) {
					value = add('"', false);
					nextChar(true);
				} else {
					add('>', true);
				}
				if (value.equals("")) {
					pw.print(value);
					pw.print('"');
					skipWhitespace();
					continue;
				}
				String urlNew = change(merge(url, value));
				if (expansion(urlNew).equals("") && !(tag.equalsIgnoreCase("img") && name.equalsIgnoreCase("src"))) {
					pw.print(value + '"');
					skipWhitespace();
					continue;
				}
				if (expansion(urlNew).equals(".php") || tag.equalsIgnoreCase("a") && name.equalsIgnoreCase("href")) {
					if (!urlNew.equals("") && depth > 1) {
						if (elements.containsKey(urlNew)) {
							pw.print("Html" + elements.get(urlNew) + ".html");
						} else {
							if (check(urlNew)) {
								pw.print("Html" + ++number + ".html");
								elements.put(urlNew, number);
								deque.push(new Triple(urlNew, depth - 1));
							} else {
								pw.print(value);
							}
						}
					} else {
						pw.print(value);
					}
				} else if (tag.equalsIgnoreCase("img") && name.equalsIgnoreCase("src")) {
					if (elements.containsKey(urlNew)) {
						pw.print("Link" + elements.get(urlNew) + ".png");
					} else {
						if (save(urlNew, "Link" + ++number + ".png")) {
							pw.print("Link" + number + ".png");
							elements.put(urlNew, number);
						} else {
							number--;
							pw.print(value);
						}
					}
				} else if (tag.equalsIgnoreCase("link") && name.equalsIgnoreCase("href")
						|| tag.equalsIgnoreCase("script") && name.equalsIgnoreCase("src")) {
					if (elements.containsKey(urlNew)) {
						pw.print("Link" + elements.get(urlNew) + expansion(urlNew));
					} else {
						String str = expansion(urlNew);
						if (urlNew.lastIndexOf('.') != -1) {
							if (save(urlNew, "Link" + ++number + str)) {
								pw.print("Link" + number + str);
								elements.put(urlNew, number);
							} else {
								number--;
								pw.print(value);
							}
						} else {
							pw.print(value);
						}
					}
				} else {
					pw.print(value);
				}
				pw.print('"');
				skipWhitespace();
			}
			if (tit && tag.equalsIgnoreCase("title")) {
				tit = false;
				title();
			}
		}

		private void close() {
			skipWhitespace();
			name();
			skipWhitespace();
		}

		private boolean check(String str) {
			try (BufferedReader b = new BufferedReader(new InputStreamReader(input.download(str), "UTF8"))) {
				if (b.read() != -1) {
					return true;
				}
			} catch (Exception e) {
			}
			return false;
		}

		private String expansion(String urlNew) {
			int i = urlNew.lastIndexOf('.') + 1;
			StringBuilder str = new StringBuilder();
			str.append('.');
			while (i < urlNew.length() && Character.isLetter(urlNew.charAt(i))) {
				str.append(urlNew.charAt(i));
				i++;
			}
			for (String s : expansion) {
				if (s.equals(str.toString())) {
					return s;
				}
			}
			return "";
		}
	}
}