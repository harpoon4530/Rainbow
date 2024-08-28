package org.rainbow.handlers;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.rainbow.models.RecordModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RecordServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(RecordServlet.class);

    private final RecordModel recordModel;

    @Inject
    public RecordServlet(RecordModel recordModel) {
        super();
        this.recordModel = recordModel;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String record = request.getPathInfo();

        if (record == null) {
            logger.error("Invalid path given; cannot query the path: {}", record);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // SC_NOT_FOUND

        int i = 0;

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int i = 0;

        recordModel.writeToDb("foobar");

    }
}
