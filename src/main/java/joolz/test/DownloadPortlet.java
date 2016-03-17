package joolz.test;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.faces.GenericFacesPortlet;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerFactory;

/**
 * Portlet implementation class ShowcasePortlet extends the generic faces
 * portlet and adds an PDF export option for showcases.
 *
 * @author hvo
 */
public class DownloadPortlet extends GenericFacesPortlet {
        private static final Log LOG = LogFactoryUtil.getLog(DownloadPortlet.class);
        private static final String EXPORT_ACTION = "showcase-export-format";
        private static final String SHOWCASE_ID = "showcase-id";

        private static final String EXPORT_TO_PDF = "pdf";

        // TODO check if objects are thread safe!
        private final FopFactory fopFactory = FopFactory.newInstance();
        private final TransformerFactory tFactory = TransformerFactory.newInstance();

        // TODO have a close look at
        // https://code.google.com/p/pdf-example/source/browse/trunk/src/main/java/comtech/pdf/PdfServlet.java?r=3

        /**
         * {@inheritDoc}
         */
        @Override
        public void serveResource(final ResourceRequest resourceRequest, final ResourceResponse resourceResponse)
                        throws PortletException, IOException {

                final String exportTypeAction = resourceRequest.getParameter(EXPORT_ACTION);

                if (EXPORT_TO_PDF.equals(exportTypeAction)) {

                        final int showcaseId = GetterUtil.getInteger(resourceRequest.getParameter(SHOWCASE_ID), 0);

                        if (showcaseId > 0) {

                                final ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

                                final HttpServletResponse servletResponse = PortalUtil.getHttpServletResponse(resourceResponse);

                                try {
                                        // Setup a buffer to obtain the content length
                                        final ByteArrayOutputStream out = new ByteArrayOutputStream();

                                        PDFUtils.exportShowcaseToPDF(showcaseId, out, themeDisplay, getPortletContext());

                                        // Prepare response
                                        servletResponse.setStatus(HttpServletResponse.SC_OK);
                                        servletResponse.setContentType("application/pdf");
                                        servletResponse.setHeader("Content-Disposition", "attachment;filename=\"showcase.pdf\"");
                                        servletResponse.setContentLength(out.size());

                                        // Send content to Browser
                                        servletResponse.getOutputStream().write(out.toByteArray());
                                        servletResponse.getOutputStream().flush();

                                } catch (final Exception e) {
                                        LOG.error(e);
                                        throw new IOException("Failed to generate: " + e.getMessage());
                                }
                        }
                        return;
                }
                super.serveResource(resourceRequest, resourceResponse);
        }


}