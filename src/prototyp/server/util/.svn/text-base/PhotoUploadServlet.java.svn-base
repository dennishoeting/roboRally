package prototyp.server.util;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

/**
 * Servlet für den Foto SingleUploader
 * 
 * @author timo
 * @version 1.0
 * 
 */
public class PhotoUploadServlet extends UploadAction {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -1387584280725361487L;

	/**
	 * Gibt eine Nummer für hochgeladene Datein. Damit diese später wieder
	 * gefunden und verschoben/umbenannt werden können.
	 */
	private static int tmpFileNumber = 0;

	/** Default */
	public PhotoUploadServlet() {

	}

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		final StringBuilder response = new StringBuilder("<response>\n");
		int cont = 0;
		for (final FileItem item : sessionFiles) {
			if (!item.isFormField() &&
			// Ist es ein Bild?
					(item.getName().toLowerCase().endsWith(".png")
							|| item.getName().toLowerCase().endsWith(".jpg")
							|| item.getName().toLowerCase().endsWith(".jpeg") || item
							.getName().toLowerCase().endsWith(".gif"))) {

				cont++;
				try {
					// Bild erstellen int tmp/tmpFileNumber und der Endung des
					// Bildes

					final File file = new File(getServletContext().getRealPath("/")
							+ File.separator
							+ "images"
							+ File.separator
							+ "temp"
							+ File.separator
							+ PhotoUploadServlet.tmpFileNumber
							+ item.getName().substring(
									item.getName().length() - 4));
					// Schreiben
					item.write(file);

					// XML für die Antwort schreiben
					response.append("<file-name>");
					response.append(file.getName());
					response.append("</file-name>\n");
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		// Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		PhotoUploadServlet.tmpFileNumber++;

		// Send information of the received files to the client.
		response.append("</response>\n");
		return response.toString();

	}

}
