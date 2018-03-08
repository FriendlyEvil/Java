package crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SimpleWebCrawler implements WebCrawler {
	private final Downloader input;
	private final Map<String, Page> pages = new HashMap();
	private final Map<String, Image> images = new HashMap();
	private final Deque<Triple> deque = new ArrayDeque<>();
	private int number = 0;

	SimpleWebCrawler(Downloader d) {
		input = d;
	}

	public String merge(String url, String s) {
		if (s.lastIndexOf("#") != -1) {
			return merge(url, s.substring(0, s.lastIndexOf("#")));
		}
		try {
			return (new URL(new URL(url), s)).toString();
		} catch (MalformedURLException e) {
			System.out.println("Error 404" + e.getMessage());
			return null;
		}
	}

	@Override
	public Page crawl(String url, int depth) {
		String urlStart = url;
		Triple trip = new Triple(url, depth);
		deque.addLast(trip);
		while (!deque.isEmpty()) {
			url = deque.peekFirst().getUrl();
			if (pages.containsKey(url)) {
				deque.removeFirst().getPage().addLink(pages.get(url));
				continue;
			}
			depth = deque.peekFirst().getDepth();
			if (depth == 0) {
				pages.put(url, new Page(url, ""));
				deque.removeFirst().getPage().addLink(pages.get(url));
				continue;
			}
			Page pageParent = deque.removeFirst().getPage();

			try (BufferedReader buff = new BufferedReader(new InputStreamReader(input.download(url), "UTF8"))) {
				Parser pars = new Parser(buff);
				pars.parse();
				Page page = new Page(url, pars.getTitle());
				if (!url.equals(urlStart)) {
					pageParent.addLink(page);
				}
				pages.put(url, page);

				for (String str : pars.getPage()) {
					deque.addLast(new Triple(merge(url, str), page, depth - 1));
				}

				for (String str : pars.getImage()) {
					String imgurl = merge(url, str);
					if (images.containsKey(imgurl)) {
						page.addImage(images.get(imgurl));
						continue;
					}
					Image image = new Image(imgurl, "Image" + ++number + ".png");
					images.put(imgurl, image);
					page.addImage(image);
					try (InputStream buf = input.download(imgurl)) {
						try (OutputStream pw = new FileOutputStream(image.getFile())) {
							byte[] buffer = new byte[1 << 16];
							int r;
							while ((r = buf.read(buffer)) != -1) {
								for (int i = 0; i < r; i++) {
									pw.write(buffer[i]);
								}
							}
						} catch (FileNotFoundException e) {
							System.out.println("Error: File '" + image.getFile() + "' can not be created or changed");
						} catch (SecurityException e) {
							System.out.println("Error: File '" + image.getFile()
									+ "' is forbidden to be created or changed by security manager");
						} catch (IOException e) {
							System.out.println("Error while working with file " + e.getMessage());
						}
					} catch (IOException e) {
						System.out.println("Error while save image " + e.getMessage());
					}
				}
			} catch (UnsupportedEncodingException e) {
				System.out.println("Unsuppording ecoding(uft-8)");
			} catch (IOException e) {
				Page page = new Page(url, "");
				pageParent.addLink(page);
				pages.put(url, page);
			}
		}
		return pages.get(urlStart);
	}
}