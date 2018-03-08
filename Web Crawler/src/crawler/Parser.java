package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	private int ch;
	private BufferedReader buff;
	private List<String> page = new ArrayList<>();
	private List<String> image = new ArrayList<>();
	private String title = "";
	private boolean tit = true;

	public Parser(BufferedReader in) {
		buff = in;
	}

	public void parse() {
		skipWhitespace();
		while (ch != -1) {
			if (check('<')) {
				if (check('!')) {
					if (check('D')) {
						nextChar();
					} else {
						skip();
					}
				} else if (check('/')) {
					close();
				} else if (Character.isLetter(ch)) {
					open();
				} else {
					nextChar();
				}
			} else {
				nextChar();
			}
		}
	}

	private void nextChar() {
		try {
			ch = buff.read();
		} catch (IOException e) {
			System.out.println("Error while working with site " + e.getMessage());
		}
	}

	private boolean check(char c) {
		if (ch == c) {
			nextChar();
			return true;
		}
		return false;
	}

	private String add(char c) {
		StringBuilder str = new StringBuilder();
		while (ch != c) {
			str.append((char) ch);
			nextChar();
		}
		return str.toString();
	}

	private void skipWhitespace() {
		while (Character.isWhitespace(ch)) {
			nextChar();
		}
	}

	private void skip() {
		while (!(check('-') && check('-') && check('>'))) {
			nextChar();
		}
	}

	private void title() {
		StringBuilder str = new StringBuilder();
		while (true) {
			if (check('<')) {
				if (check('/')) {
					break;
				}
				else {
					str.append('<');
				}
			}else {
				str.append((char)ch);
				nextChar();
			}
		}
		title = change(str.toString());
		close();
	}
	
	private String name() {
		StringBuilder sb = new StringBuilder();
		while (Character.isLetter(ch) || ch == '-' || ch == '_' || Character.isDigit(ch) || ch == ':' || ch == '.') {
			sb.append((char) ch);
			nextChar();
		}
		return sb.toString();
	}

	private String change(String str) {
		return str.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&")
				.replaceAll("&mdash;", "\u2014").replaceAll("&nbsp;", "\u00A0").replaceAll("&reg;", "\u00AE");
	}
	
	private void open() {
		String tag = name();
		skipWhitespace();
		while (!check('>') && !(check('/') && check('>'))) {
			String name = name();
			skipWhitespace();
			nextChar();
			skipWhitespace();
			String Value = "";
			if (check('"')) {
				Value = add('"');
				nextChar();
			} else {
				add('>');
			}
			if (tag.equalsIgnoreCase("a") && name.equalsIgnoreCase("href")) {
				page.add(change(Value));
			} else if (tag.equalsIgnoreCase("img") && name.equalsIgnoreCase("src")) {
				image.add(change(Value));
			}
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

	public String getTitle() {
		return title;
	}

	public List<String> getPage() {
		return page;
	}

	public List<String> getImage() {
		return image;
	}
}
