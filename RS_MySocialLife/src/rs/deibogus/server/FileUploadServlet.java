package rs.deibogus.server;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import rs.deibogus.server.socialmanager.FlickrManager;
import rs.deibogus.server.socialmanager.ISocialManager;
import rs.deibogus.server.socialmanager.PicasaManager;
import rs.deibogus.server.socialmanager.SocialManager;
import rs.deibogus.shared.Foto;
import rs.deibogus.shared.SessionData;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 4646930280989636350L;
	private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";
	private static final String DEFAULT_TEMP_DIR = ".";

	SessionData profile;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ISocialManager socialManager = null;
		HttpSession session = req.getSession();
		
		profile = (SessionData)session.getAttribute("session");
		//System.out.println("TESTE NA SERVLET: ----- " + profile.getCatalogo().size());
		
		// process only multipart requests
		if (ServletFileUpload.isMultipartContent(req)) {
			
			System.out.println("1o IF");

			File tempDir = getTempDir();
			if (!tempDir.exists()) {
				tempDir.mkdirs();
			}

			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			try {
				System.out.println("Dentro do try!!!");
				List<FileItem> items = upload.parseRequest(req);
				for (FileItem fileItem : items) {
					System.out.println("Dentro do for!!!");
					// process only file upload
					if (fileItem.isFormField()) continue;
					String fileName = fileItem.getName();
					// get only the file name not whole path
					if (fileName != null) {
						fileName = FilenameUtils. getName(fileName);
					}

					File uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
					if (uploadedFile.createNewFile()) {
						fileItem.write(uploadedFile);
						
						//Flickr
						Foto foto = new Foto("flickr", fileName);
						socialManager = new SocialManager(new FlickrManager(profile.getFlickrAuth()));
						socialManager.uploadPhoto(foto,uploadedFile);
						profile.getCatalogo().add(foto);
						
						//Picasa
						Foto foto2 = new Foto("picasa", fileName);
						socialManager = new SocialManager(new PicasaManager(profile.getService()));
						socialManager.uploadPhoto(foto,uploadedFile);
						profile.getCatalogo().add(foto2);
						
						//apapgar a foto
						uploadedFile.delete();
						
						resp.setStatus(HttpServletResponse.SC_CREATED);
						resp.getWriter().print("The file was created successfully.");
						resp.flushBuffer();
						System.out.println("Dentro do ultimo if!!!");
					} else
						throw new IOException("The file already exists in repository.");
				}
			} catch (Exception e) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"An error occurred while creating the file : " + e.getMessage());
			}

		} else {
			resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
					"Request contents type is not supported by the servlet.");
		}
	}

	private File getTempDir() {
		return new File(DEFAULT_TEMP_DIR);
	}
}