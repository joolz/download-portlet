package joolz.test;

import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class DownloadBean {
	private static final Log LOG = LogFactoryUtil.getLog(DownloadBean.class);
	private static final String TEST_FILE = "download-test-";
	private File tempFile;
	private boolean ready = false;
	private static final int TWO_SECONDS = 1000 * 2;
	private int progress = 0;
	private boolean pollerStopped = false;

	public String doGenerate() {
		final LiferayFacesContext lfc = LiferayFacesContext.getInstance();
		try {
			tempFile = File.createTempFile(TEST_FILE, null);
			tempFile.deleteOnExit();
			LOG.debug("Created tempfile " + tempFile.getCanonicalPath());
			final StringBuilder sb = new StringBuilder();
			sb.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
			sb.append("\n");
			sb.append("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?");
			sb.append("\n\n");
			for (int i = 0; i < 5; i++) {
				setProgress(progress + 20);
				final String append = i + "\n" + sb.toString();
				Files.write(Paths.get(tempFile.getCanonicalPath()), append.getBytes(), StandardOpenOption.APPEND);
				LOG.debug("Appended " + i + ", now sleep " + TWO_SECONDS + " milliseconds");
				Thread.sleep(TWO_SECONDS);
			}
			LOG.debug("Preparation finished");
			ready = true;
			lfc.addGlobalInfoMessage("Generated " + tempFile.getName());
		} catch (final IOException | InterruptedException e) {
			LOG.error(e);
			lfc.addGlobalUnexpectedErrorMessage();
		}
		return StringPool.BLANK;
	}

	public File getTempFile() {
		return tempFile;
	}

	public boolean isReady() {
		return ready;
	}

	public void poller() {
		LOG.debug("poller called, ready is " + ready);
	}

	public void handleComplete() {
		LOG.debug("Progress complete");
	}

	public void setProgress(final int counter) {
		LOG.debug("Set progress to " + counter);
		progress = counter;
	}

	public int getProgress() {
		LOG.debug("Got progress " + progress);
		return progress;
	}

	public void pollerListener() {
		if (ready) {
			pollerStopped = true;
		}
	}

	public boolean isPollerStopped() {
		return pollerStopped;
	}

}