package net.cox.messaging.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cox.api.SendApi;
import net.cox.messaging.services.TcpSenderService;
import net.cox.messaging.util.Base64Utility;
import net.cox.model.SendTcpData200Response;
import net.cox.model.TcpData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class TcpSenderController implements SendApi {
    private TcpSenderService tcpSenderService;

    @Override
    public ResponseEntity<SendTcpData200Response> sendTcpData(TcpData tcpData) {
        try {
            byte[] byteArrayReturn = tcpSenderService.send(tcpData);

            if (byteArrayReturn != null) {
                SendTcpData200Response response = new SendTcpData200Response();
                response.binaryData(Base64Utility.encode(byteArrayReturn));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
