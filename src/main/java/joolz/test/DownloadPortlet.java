package joolz.test;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.util.PortalUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.faces.GenericFacesPortlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class DownloadPortlet extends GenericFacesPortlet {
	private static final Log LOG = LogFactoryUtil.getLog(DownloadPortlet.class);

	@Override
	public void serveResource(final ResourceRequest resourceRequest, final ResourceResponse resourceResponse)
			throws PortletException, IOException {

		LOG.debug("Custom serve resource");

		final String fileName = resourceRequest.getParameter("fileName");

		LOG.debug("Got filename " + fileName);

		if (fileName != null && fileName.trim().length() > 0) {

			final HttpServletResponse servletResponse = PortalUtil.getHttpServletResponse(resourceResponse);

			try {
				// Setup a buffer to obtain the content length
				final ByteArrayOutputStream out = new ByteArrayOutputStream();

				// Prepare response
				servletResponse.setStatus(HttpServletResponse.SC_OK);
				servletResponse.setContentType("application/pdf");
				servletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
				servletResponse.setContentLength(out.size());

				// Send content to Browser
				final String tempdir = System.getProperty("java.io.tmpdir");
				final BufferedInputStream input = new BufferedInputStream(new FileInputStream(tempdir + File.separator
						+ fileName));
				IOUtils.copy(input, servletResponse.getOutputStream());

			} catch (final Throwable e) {
				LOG.error(e);
				servletResponse.reset();
				servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				servletResponse.getWriter().append("Error generating document: " + e.getMessage());
			} finally {
				servletResponse.flushBuffer();
			}
			return;
		}
		super.serveResource(resourceRequest, resourceResponse);
	}

}