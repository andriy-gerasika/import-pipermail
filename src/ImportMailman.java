import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

public class ImportMailman {

	public static void main(String[] args) throws TransformerException, SAXException, IOException {
		if (args.length != 2) {
			System.err.println("usage: archive-url archive-file");
		}
		String archiveUrl = args[0];
		File archiveFile = new File(args[1]);

		System.out.println("downloading " + archiveUrl);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(
				new StreamSource(ImportMailman.class.getResourceAsStream("import-mailman.xsl")));
		StringWriter writer = new StringWriter();
		transformer.transform(
				new SAXSource(XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser"),
						new InputSource(archiveUrl)), new StreamResult(writer));

		String[] urls = writer.toString().split("\n");
		OutputStream archiveStream = new FileOutputStream(archiveFile);
		try {
			for (String url : urls) {
				System.out.println("downloading " + url);
				InputStream inputStream = new URL(archiveUrl + url).openStream();
				try {
					if (url.endsWith(".gz")) {
						inputStream = new GZIPInputStream(inputStream);
					}
					transfuse(inputStream, archiveStream);
				} finally {
					inputStream.close();
				}
			}
		} finally {
			archiveStream.close();
		}
		System.out.println("ok");
	}

	private static void transfuse(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] buffer = new byte[65535];
		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, bytesRead);
		}
	}
}
