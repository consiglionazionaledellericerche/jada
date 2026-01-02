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

package it.cnr.jada.util.mail;

import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.*;

public class PECBodyParts {

    private final List<DataHandler> attachments = new Vector<DataHandler>();
    private final HashMap<String, BodyPart> mapBodyPart = new HashMap<String, BodyPart>();
    private BodyPart bodyTextHTML = null;
    private BodyPart bodyTextPlain = null;

    public final List<DataHandler> getAttachments() {
        return this.attachments;
    }

    public BodyPart getBodyPart(final String keyContentType) {
        return this.mapBodyPart.get(keyContentType);
    }

    public final BodyPart getBodyTextHTML() {
        return this.bodyTextHTML;
    }

    public final BodyPart getBodyTextPlain() {
        return this.bodyTextPlain;
    }

    public int getCountBodyParts() {
        return this.mapBodyPart.size();
    }

    public DataHandler getDataHandlerByPartKey(final String keyContentType)
            throws MessagingException {
        final BodyPart bodyPiece = this.mapBodyPart.get(keyContentType);
        final DataHandler dataHandler = bodyPiece.getDataHandler();

        return dataHandler;
    }

    public Set<String> getKeysContentType() {
        return this.mapBodyPart.keySet();
    }

    public void putBodyPart(final BodyPart bodyPiece) throws MessagingException {
        this.mapBodyPart.put(bodyPiece.getContentType(), bodyPiece);

    }

    public void setBodyPartsMime() throws MessagingException, IOException {
        final List<DataHandler> listDataSource = new Vector<DataHandler>();
        final Set<String> listKeyContentType = getKeysContentType();
        final Iterator<String> iterKeyContentType = listKeyContentType
                .iterator();

        while (iterKeyContentType.hasNext()) {
            listDataSource.add(getDataHandlerByPartKey(iterKeyContentType
                    .next()));
        }
        this.attachments.clear();
        for (int i = 0; i < listDataSource.size(); i++) {
            final DataHandler dataHandler = listDataSource.get(i);
            if (dataHandler.getContentType().matches(".*multipart.*")) {
                final MimeMultipart multiPart = (MimeMultipart) dataHandler
                        .getContent();
                BodyPart bodyPieceMime;
                for (int j = 0; j < multiPart.getCount(); j++) {
                    bodyPieceMime = multiPart.getBodyPart(j);
                    if (bodyPieceMime.getContentType()
                            .matches(".*text/plain.*")) {
                        this.bodyTextPlain = bodyPieceMime;
                    } else if (bodyPieceMime.getContentType().matches(
                            ".*text/html.*")) {
                        this.bodyTextHTML = bodyPieceMime;
                    }
                }
            } else {
                final String name = getAttachmentName(dataHandler);
                if ((name != null) && !name.equals("daticert.xml")) {
                    this.attachments.add(dataHandler);
                }
            }
        }

    }

    private String getAttachmentName(final DataHandler dataHandler) {
        String name = dataHandler.getContentType();
        final String[] splitOne = name.split(";");
        final String[] splitTwo = splitOne[1].split("=");
        name = splitTwo[1];
        return name;
    }

}