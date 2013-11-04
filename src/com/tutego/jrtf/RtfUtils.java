package com.tutego.jrtf;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Collection of utilities for jRTF API.
 * 
 * @author pedro.costa@xseed.com.br
 */
public class RtfUtils {
	
	/**
	 * Opens a file using system default file editor.
	 * @param file File to be open.
	 * @return <code>true</code> if operation was sucessful or <code>false</code> otherwise.
	 */
	public static boolean openSystemDefaultEditor(final File file) {
		if (!Desktop.isDesktopSupported()) {
			return false;
		}

		Desktop desktop = Desktop.getDesktop();
		if (!desktop.isSupported(Desktop.Action.EDIT)) {
			return false;
		}

		try {
			desktop.browse(file.toURI());
		} catch (IOException e) {
			// Log an error
			return false;
		}

		return true;
	}
	
	/**
	 * Reads a file and returns its content as a String.
	 * 
	 * @author pedro.costa@xseed.com.br
	 */
	public static String readFile(File file) throws Exception {		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		reader.close();

		return stringBuilder.toString();
	}
	
	/**
	 * This code is a port from a PHP version obtained from this article: <a
	 * href="http://webcheatsheet.com/php/reading_the_clean_text_from_rtf.php"
	 * >PHP: Reading the "clean" text from RTF</a>
	 * 
	 * Variable names, code style, etc were preserved as possible, but for some 
	 * PHP constructs not present in Java some modification was necessary.
	 * 
	 * @author pedro.costa@xseed.com.br
	 */
	public static String rtf2text(File file) throws Exception {
		return rtf2text(readFile(file));
	}
	
	/**
	 * This code is a port from a PHP version obtained from this article: <a
	 * href="http://webcheatsheet.com/php/reading_the_clean_text_from_rtf.php"
	 * >PHP: Reading the "clean" text from RTF</a>
	 * 
	 * Variable names, code style, etc were preserved as possible, but for some 
	 * PHP constructs not present in Java some modification was necessary. 
	 * 
	 * @author pedro.costa@xseed.com.br
	 */
	@SuppressWarnings("unchecked")
	public static String rtf2text(String text) throws Exception {
		if (text == null || text.length() == 0) {
			return "";
		}
		
		// Create empty stack array.
		String document = "";
		Map<String, Object> stack = new LinkedHashMap<String, Object>();
		int j = -1;
		
		// Read the data character-by- character…
		for (int i = 0, len = text.length(); i < len; i++) {
			char c = text.charAt(i);
			// Depending on current character select the further actions.
			switch (c) {
			// the most important key word backslash
			case '\\':
				// read next character
				char nc = text.charAt(i + 1);
				
				// If it is another backslash or nonbreaking space or hyphen,
                // then the character is plain text and add it to the output stream.
				if (nc == '\\' && rtf_isPlainText(stack.get(String.valueOf(j)))) document += '\\';
				else if (nc == '~' && rtf_isPlainText(stack.get(String.valueOf(j)))) document += ' ';
				else if (nc == '_' && rtf_isPlainText(stack.get(String.valueOf(j)))) document += '-';
				// If it is an asterisk mark, add it to the stack.
                else if (nc == '*') {
                	// $stack[$j]["*"] = true;
                	Map<String, Object> innerStack = (Map<String, Object>) stack.get(String.valueOf(j));
                	if (innerStack == null) {
                		innerStack = new LinkedHashMap<String, Object>();
						stack.put(String.valueOf(j), innerStack);
                	}
					innerStack.put("*", true);
				}
				// If it is a single quote, read next two characters that are the hexadecimal notation
                // of a character we should add to the output stream.
                else if (nc == '\'') {
					String hex = text.substring(i + 2, i + 4);
					if (rtf_isPlainText(stack.get(String.valueOf(j)))) {
						document += (char) Integer.parseInt(hex, 16);
					}
					//Shift the pointer.
					i += 2;
				}
				// Since, we’ve found the alphabetic character, the next characters are control word
                // and, possibly, some digit parameter.
                else if (nc >= 'a' && nc <= 'z' || nc >= 'A' && nc <= 'Z') {
                    String word = "";
                    String param = "";

                    // Start reading characters after the backslash.
                    int m = 0;
                    for (int k = i + 1; k < text.length(); k++, m++) {
                        nc = text.charAt(k);
                        // If the current character is a letter and there were no digits before it,
                        // then we’re still reading the control word. If there were digits, we should stop
                        // since we reach the end of the control word.
                        if (nc >= 'a' && nc <= 'z' || nc >= 'A' && nc <= 'Z') {
                            if (param == null || param.length() == 0) {
                                word += nc;
                            } else {
                                break;
                            }
                        // If it is a digit, store the parameter.
                        } else if (nc >= '0' && nc <= '9') {
                            param += nc;
                        // Since minus sign may occur only before a digit parameter, check whether
                        // param is empty. Otherwise, we reach the end of the control word.
                        } else if (nc == '-') {
                            if (param == null || param.length() == 0) {
                                param += nc;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    // Shift the pointer on the number of read characters.
                    i += m - 1;

                    // Start analyzing what we’ve read. We are interested mostly in control words.
                    String toText = "";
                    
                    // If the control word is "u", then its parameter is the decimal notation of the
                    // Unicode character that should be added to the output stream.
                    // We need to check whether the stack contains \ ucN control word. If it does,
                    // we should remove the N characters from the output stream.
                    if ((word = word.toLowerCase()).equals("u")) {
                    	toText += (char) Integer.parseInt(param, 16);
//                    	$ucDelta = @$stack[$j]["uc"];
//                    	if ($ucDelta > 0)
//                    		$i += $ucDelta;
                    	try {
                    		Integer ucDelta = (Integer) ((Map<String, Object>) stack.get(String.valueOf(j))).get("uc");
                    		if (ucDelta != null && ucDelta > 0)
                                i += ucDelta;
						} catch (Exception e) {}
                    }
                    // Select line feeds, spaces and tabs.
                    else if (word.equals("par") || word.equals("page") || word.equals("column") || word.equals("line") || word.equals("lbr")) {
                    	toText += "\n"; 
					} else if ( word.equals("emspace") || word.equals("enspace") || word.equals("qmspace") ) {
		            	toText += " "; 
					} else if ( word.equals("tab")) toText += "\t"; 
		            // Add current date and time instead of corresponding labels.
					else if ( word.equals("chdate")) toText += new SimpleDateFormat("dd/MM/yyyy").format(new Date());
					else if ( word.equals("chdpl")) toText += new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(new Date());
					else if ( word.equals("chdpa")) toText += new SimpleDateFormat("EEE, MMM dd, yyyy").format(new Date());
					else if ( word.equals("chtime")) toText += new SimpleDateFormat("HH:mm:ss").format(new Date());
                    // Replace some reserved characters to their html analogs.
					else if ( word.equals("emdash")) toText += Character.toString((char) 8212);
					else if ( word.equals("endash")) toText += Character.toString((char) 8211);
					else if ( word.equals("bullet")) toText += Character.toString((char) 8226);
					else if ( word.equals("lquote")) toText += Character.toString((char) 8216);
					else if ( word.equals("rquote")) toText += Character.toString((char) 8217);
					else if ( word.equals("ldblquote")) toText += Character.toString((char) 171);
					else if ( word.equals("rdblquote")) toText += Character.toString((char) 187);
                    // Add all other to the control words stack. If a control word
                    // does not include parameters, set &param to true.
					else {
//						$stack[$j][strtolower($word)] = empty($param) ? true : $param;
						Map<String, Object> innerStack = (Map<String, Object>) stack.get(String.valueOf(j));
						if (innerStack == null) {
	                		innerStack = new LinkedHashMap<String, Object>();
							stack.put(String.valueOf(j), innerStack);
	                	}
						innerStack.put(word.toLowerCase(), param == null || param.length() == 0 ? Boolean.TRUE : param);
					}
                    
                    // Add data to the output stream if required.
                    if (rtf_isPlainText(stack.get(String.valueOf(j)))) {
                        document += toText;
                    }
                }
                
                i++;
				
			break;
			// If we read the opening brace {, then new subgroup starts and we add
            // new array stack element and write the data from previous stack element to it.
			case '{':
//				array_push($stack, $stack[$j++]);
				Map<String, Object> innerStack = (Map<String, Object>) stack.get(String.valueOf(j));
				if (innerStack == null) {
					innerStack = new LinkedHashMap<String, Object>();
					stack.put(String.valueOf(j), innerStack);
				}
				j++;
				Map<String, Object> newStack = new LinkedHashMap<String, Object>();
				for (Iterator<String> it = innerStack.keySet().iterator(); it.hasNext();) {
					String key = it.next();
					Object value = innerStack.get(key);
					newStack.put(key, value);
				}
				
				stack.put(String.valueOf(j), newStack);
				
			break;
	        // If we read the closing brace }, then we reach the end of subgroup and should remove 
            // the last stack element.
            case '}':
//            	array_pop($stack);
//                $j--;
            	stack.remove(String.valueOf(j));
				j--;
			break;
			// Skip “trash”.
            case '\0': case '\r': case '\f': case '\n': break;
           	// Add other data to the output stream if required.
			default:
				if (rtf_isPlainText(stack.get(String.valueOf(j)))) {
					document += c;
				}
			break;
			}
		}
		// Return result.
		return document;
	}

	/**
	 * Function that checks whether the data are the on-screen text. It works in
	 * the following way: an array arrfailAt stores the control words for the
	 * current state of the stack, which show that input data are something else
	 * than plain text. For example, there may be a description of font or color
	 * palette etc.
	 */
	@SuppressWarnings("unchecked")
	private static boolean rtf_isPlainText(Object s) {
		String[] arrfailAt = { "*", "fonttbl", "colortbl", "datastore", "themedata" };
		for (int i = 0; i < arrfailAt.length; i++) {
			if (s != null && s instanceof Map<?, ?>	&& ((Map<String, Object>) s).get(arrfailAt[i]) != null) {
				return false;
			}
		}
		return true;
	}

}
