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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MultipartResponse {

    HttpServletResponse res;
    ServletOutputStream out;
    boolean endedLastResponse;

    public MultipartResponse(HttpServletResponse response)
            throws IOException {
        endedLastResponse = true;
        res = response;
        out = res.getOutputStream();
        res.setContentType("multipart/x-mixed-replace;boundary=End");
        out.println();
        out.println("--End");
    }

    public void endResponse()
            throws IOException {
        out.println();
        out.println("--End");
        out.flush();
        endedLastResponse = true;
    }

    public void finish()
            throws IOException {
        out.println("--End--");
        out.flush();
    }

    public void startResponse(String contentType)
            throws IOException {
        if (!endedLastResponse)
            endResponse();
        out.println("Content-type: " + contentType);
        out.println();
        endedLastResponse = false;
    }
}