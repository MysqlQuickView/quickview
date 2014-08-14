package lad.eclipse.action;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class EditorUtils {
	private static ITextSelection selection;
	private static AbstractTextEditor editor;
	private static IDocument document;

	public static void insert(String addStr) {
		initDoc();
		try {
			document.replace(selection.getOffset(), selection.getLength(),
					addStr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		setSelection(selection.getOffset(), addStr.length());
		editor.setFocus();
	}
	
	public static String getUrl() {
		initDoc();
		return editor.getEditorInput().getToolTipText();
	}
	
	public static String getSelectionText() {
		initDoc();
		String selectStr = selection.getText();
		return selectStr;
	}

	public static String getEmptyString() {
		initDoc();
		try {
			IRegion region = document.getLineInformationOfOffset(selection
					.getOffset());
			String result = document.get(region.getOffset(),
					selection.getOffset() - region.getOffset());
			String emptuStr = "";
			for (char c : result.toCharArray()) {
				if ((c != ' ') && (c != '\t'))
					break;
				emptuStr = emptuStr + c;
			}

			return emptuStr;
		} catch (BadLocationException localBadLocationException) {
		}
		return "";
	}

	public static void replace(String beforeStr, String afterStr) {
		initDoc();

		String replaceStr = "";
		try {
			IRegion region = document.getLineInformationOfOffset(selection
					.getOffset());
			String selectStr = selection.getText();

			String emptyStr = document.get(region.getOffset(),
					selection.getOffset() - region.getOffset());

			replaceStr = beforeStr + "\r\n" + emptyStr + selectStr + "\r\n"
					+ emptyStr + afterStr;

			document.replace(selection.getOffset(), selection.getLength(),
					replaceStr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		setSelection(selection.getOffset(), replaceStr.length());
		editor.setFocus();
	}

	public static void replaceRegex(String regexStr, String repStr) {
		initDoc();

		String context = document.get();
		String newContent = context.replaceAll(regexStr, repStr);
		try {
			document.replace(0, context.length(), newContent);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		editor.setFocus();
	}

	public static String getRegStr(String reg,String content) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()){
			return matcher.group(1);
		}else{
			return "";
		}
	}
	
	private static void initDoc() {
		IEditorPart editorpart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		selection = (ITextSelection) editorpart.getEditorSite()
				.getSelectionProvider().getSelection();

		editor = (AbstractTextEditor) editorpart;

		document = editor.getDocumentProvider().getDocument(
				editor.getEditorInput());
	}

	public static void setSelection(int offset, int length) {
		ITextSelection tsNew = new TextSelection(document, offset, length);
		editor.getEditorSite().getSelectionProvider().setSelection(tsNew);
	}

	public static void _consolePrint(String str) {
		MessageConsole console = new MessageConsole("replace Console", null);
		MessageConsoleStream consoleStream = console.newMessageStream();
		consoleStream.println(str);
	}
	
	/**
	 * 从剪切板获得文字。
	 */
	public static String getSysClipboardText() {
		String ret = "";
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 获取剪切板中的内容
		Transferable clipTf = sysClip.getContents(null);

		if (clipTf != null) {
			// 检查内容是否是文本类型
			if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					ret = (String) clipTf
							.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return ret;
	}

	/**
	 * 将字符串复制到剪切板。
	 */
	public static void setSysClipboardText(String writeMe) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
		clip.setContents(tText, null);
	}

	/**
	 * 从剪切板获得图片。
	 */
	public static Image getImageFromClipboard() throws Exception {
		Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable cc = sysc.getContents(null);
		if (cc == null)
			return null;
		else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
			return (Image) cc.getTransferData(DataFlavor.imageFlavor);
		return null;
	}

	/**
	 * 复制图片到剪切板。
	 */
	public static void setClipboardImage(final Image image) {
		Transferable trans = new Transferable() {
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}

			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor))
					return image;
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans,
				null);
	}
}