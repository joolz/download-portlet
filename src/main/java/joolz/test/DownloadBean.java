package joolz.test;

import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class DownloadBean {
	private static final Log LOG = LogFactoryUtil.getLog(DownloadBean.class);
	private static final String TEST_FILE = "download-test-";
	private File tempFile;
	private StreamedContent file;

	@PostConstruct
	public void init() {
		try {
			final LiferayFacesContext lfc = LiferayFacesContext.getInstance();
			tempFile = File.createTempFile(TEST_FILE, null);
			tempFile.deleteOnExit();
			LOG.debug("Created tempfile " + tempFile.getCanonicalPath());
			Files.write(Paths.get(tempFile.getCanonicalPath()), getTestContent().getBytes(), StandardOpenOption.APPEND);
			final InputStream stream = ((ServletContext) lfc.getExternalContext().getContext())
					.getResourceAsStream(tempFile.getCanonicalPath());
			file = new DefaultStreamedContent(stream, "text/plain", tempFile.getName());
		} catch (final IOException e) {
			LOG.error(e);
		}
	}

	public StreamedContent getFile() {
		return file;
	}

	private String getTestContent() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		sb.append("\n");
		sb.append("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?");
		return sb.toString();
	}

}