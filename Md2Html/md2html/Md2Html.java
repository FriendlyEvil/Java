package md2html;

import java.io.*;
import java.util.*;

public class Md2Html {
	private static final String[] html = { "em", "strong", "em", "strong", "", "s", "", "u", "code", "mark" };
	private static char[] markdown = { '*', '*', '_', '_', '-', '-', '+', '+', '`', '~', ' ' };
	private static int[] ind = { 0, 2, 4, 6, 8, 9 };
	
	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("We need 2 arguments");
			return;
		}

		try (BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF8"))) {
			try (PrintWriter print = new PrintWriter(new File(args[1]), "UTF8")) {

				int ch = 0;
				int ch1 = 0;
				boolean par = true;
				boolean read = true;
				Deque<String> deque = new ArrayDeque<>();

				while (ch != -1) {
					if (read) {
						ch = buff.read();
					} else {
						read = true;
						ch = ch1;
					}

					if (par) {
						while (ch == '\r' || ch == '\n') {
							ch = buff.read();
						}
						int lvl = 0;
						ch1 = ch;
						while (ch1 == '#') {
							lvl++;
							ch1 = buff.read();
						}
						if (!Character.isWhitespace(ch1) || lvl == 0) {
							print.print("<p>");
							deque.push("</p>");
							read = false;
							for (int i = 0; i < lvl; i++) {
								print.print("#");
							}
						} else {
							print.print("<h" + lvl + ">");
							deque.push("</h" + lvl + ">");
						}
						par = false;
						continue;
					}

					if (ch == '\n' || ch == '\r') {
						ch1 = buff.read();
						if (ch == '\r' && ch1 == '\n') {
							ch1 = buff.read();
						}
						if (ch1 == -1 || ch1 == '\n' || ch1 == '\r') {
							print.print(deque.removeFirst());
							print.println();
							if (deque.size() != 0) {
								System.out.println("You forgot to close the tag");
							}
							while (ch != -1 && (ch1 == '\n' || ch1 == '\r'))
								ch1 = buff.read();
							par = true;
						} else {
							print.println();
						}
						read = false;
						continue;
					}

					if (ch == '*' || ch == '_' || ch == '+' || ch == '-' || ch == '`' || ch == '~') {
						int i = Arrays.asList(markdown).indexOf(ch);
						int lvl = 1;
						if (buff.ready()) {
							ch1 = buff.read();
						}
						while (ch1 == markdown[i + 1] && markdown[i] == markdown[i + 1] && buff.ready()) {
							i++;
							lvl++;
							ch1 = buff.read();
						}

						if (html[i] != "") {
							if (deque.size() != 0 && deque.peekFirst().startsWith(ch + "")) {
								deque.pop();
								print.print("</" + html[i] + ">");
							} else {
								if (!Character.isWhitespace(ch1)) {
									print.print("<" + html[i] + ">");
									deque.push(ch + html[i]);	
								} else {
									for (int t = 0; t < lvl; t++) {
										print.print((char)ch);
									}
								}
							}
						} else {
							for (int t = 0; t < lvl; t++) {
								print.print((char)ch);
							}
						}
						read = false;
						continue;
					}

					switch (ch) {
					case '<':
						print.print("&lt;");
						break;
					case '>':
						print.print("&gt;");
						break;
					case '&':
						print.print("&amp;");
						break;
					case '\\':
						ch1 = buff.read();
						if (ch1 != -1) {
							print.print((char)ch1);
						} else {
							print.print('/');
						}
						break;
					default:
						print.print((char)ch);
						break;
					}

				}

				if (deque.size() != 0) {
					print.print(deque.peekFirst());
					if (deque.size() != 0) {
						System.out.println("You forgot to close the tag");
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Error: File '" + args[1] + "' can not be created or changed");
			} catch (SecurityException e) {
				System.out.println(
						"Error: File '" + args[1] + "' is forbidden to be created or changed by security manager");
			} catch (UnsupportedEncodingException e) {
				System.out.println("Unsuppording ecoding(uft-8)");
			} catch (IOException e) {
				System.out.println("Error while working with files " + e.getMessage());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: File'" + args[0] + "' can not be created or changed");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unsuppording ecoding(uft-8)");
		} catch (IOException e) {
			System.out.println("Error while working with files " + e.getMessage());
		}
	}
}
