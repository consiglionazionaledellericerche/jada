/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.util.upload;

import it.cnr.jada.util.servlet.MultipartWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class MultipartFilter
        implements Filter {

    private FilterConfig config;
    private String dir;

    public MultipartFilter() {
        config = null;
        dir = null;
    }

    public void destroy() {
        config = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String type = req.getHeader("Content-Type");
        if (type == null || !type.startsWith("multipart/form-data")) {
            chain.doFilter(request, response);
        } else {
            MultipartWrapper multi = new MultipartWrapper(req, dir);
            chain.doFilter(multi, response);
        }
    }

    public void init(FilterConfig config)
            throws ServletException {
        this.config = config;
        dir = config.getInitParameter("uploadDir");
        if (dir == null) {
            File tempdir = (File) config.getServletContext().getAttribute("javax.servlet.context.tempdir");
            if (tempdir != null)
                dir = tempdir.toString();
            else
                throw new ServletException("MultipartFilter: No upload directory found: set an uploadDir init parameter or ensure the javax.servlet.context.tempdir directory is valid");
        }
    }
}