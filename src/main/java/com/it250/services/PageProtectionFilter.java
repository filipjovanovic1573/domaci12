package com.it250.services;

import com.it250.entities.User;
import com.it250.other.Role;
import com.it250.pages.Login;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.security.RolesAllowed;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

public class PageProtectionFilter implements ComponentRequestFilter {

    private final String autoLoginStr = System.getProperty("jumpstart.auto-login");
    private final PageRenderLinkSource pageRenderLinkSource;
    private final ComponentSource componentSource;
    private final Request request;
    private final Response response;
    private ApplicationStateManager sessionStateManager;
    private final Logger logger;

    public PageProtectionFilter(PageRenderLinkSource pageRenderLinkSource, ComponentSource componentSource, Request request, Response response, ApplicationStateManager sessionStateManager, Logger logger) {
        this.pageRenderLinkSource = pageRenderLinkSource;
        this.componentSource = componentSource;
        this.request = request;
        this.response = response;
        this.sessionStateManager = sessionStateManager;
        this.logger = logger;
    }

    @Override
    public void handleComponentEvent(ComponentEventRequestParameters cerp, ComponentRequestHandler crh) throws IOException {
        if (isAuthorisedToPage(cerp.getActivePageName(), cerp.getEventContext())) {
            crh.handleComponentEvent(cerp);
        } else {
            return;
        }
    }

    @Override
    public void handlePageRender(PageRenderRequestParameters prrp, ComponentRequestHandler crh) throws IOException {
        if (isAuthorisedToPage(prrp.getLogicalPageName(), prrp.getActivationContext())) {
            crh.handlePageRender(prrp);
        } else {
            return;
        }
    }

    public boolean isAuthorisedToPage(String requestedPageName, EventContext eventContext) throws
            IOException {
        Component page = componentSource.getPage(requestedPageName);
        boolean protectedPage = page.getClass().getAnnotation(ProtectedPage.class) != null;
        RolesAllowed rolesAllowed = page.getClass().getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null && !protectedPage) {
            throw new IllegalStateException("Page \"" + requestedPageName
                    + "\" is annotated with @RolesAllowed but not @ProtectedPage.");
        }
        if (!protectedPage) {
            return true;
        } else if (request.isXHR() && request.getSession(false) == null) {
            OutputStream os = response.getOutputStream("application/json;charset=UTF-8");
            os.write("{\"script\":\"window.location.reload();\"}".getBytes());
            os.flush();
            return false;
        } else if (isAuthenticated()) {
            if (isAuthorised(rolesAllowed)) {
                return true;
            } else {
                Link pageProtectedLink
                        = pageRenderLinkSource.createPageRenderLinkWithContext(Login.class,
                                requestedPageName);
                response.sendRedirect(pageProtectedLink);
                return false;
            }
        } else {
            Link loginPageLink = pageRenderLinkSource.createPageRenderLink(Login.class);
            response.sendRedirect(loginPageLink);
            return false;
        }
    }

    private Link makeLinkToRequestedPage(String requestedPageName, EventContext eventContext) {
        Link linkToRequestedPage;
        if (eventContext instanceof EmptyEventContext) {
            linkToRequestedPage = pageRenderLinkSource.createPageRenderLink(requestedPageName);
        } else {
            Object[] args = new String[eventContext.getCount()];
            for (int i = 0; i < eventContext.getCount(); i++) {
                args[i] = eventContext.get(String.class, i);
            }
            linkToRequestedPage
                    = pageRenderLinkSource.createPageRenderLinkWithContext(requestedPageName, args);
        }
        return linkToRequestedPage;
    }

    private boolean isAuthenticated() throws IOException {
        User user = sessionStateManager.getIfExists(User.class);
        if (user != null) {
            if (user.getRole() == Role.Admin || user.getRole() == Role.Korisnik) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthorised(RolesAllowed rolesAllowed) throws IOException {
        boolean authorised = false;
        if (rolesAllowed == null) {
            authorised = true;
        } else {
            User user = sessionStateManager.getIfExists(User.class);
            if (user == null) {
                authorised = false;
            } else {
                Role rola = user.getRole();
                for (String i : rolesAllowed.value()) {
                    if (i.equalsIgnoreCase(rola.name())) {
                        authorised = true;
                    }
                }
            }
        }
        return authorised;
    }
}
