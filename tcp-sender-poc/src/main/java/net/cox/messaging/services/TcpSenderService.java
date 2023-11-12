package net.cox.messaging.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cox.messaging.constants.Constants;
import net.cox.messaging.protocol.TCPSender;
import net.cox.messaging.util.Base64Utility;
import net.cox.model.TcpData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class TcpSenderService {
    private final TCPSender tcpSender;

    public byte[] send(TcpData tcpData) {
        byte[] outgoing = Base64Utility.decode(tcpData.getBinaryData());
        return sendInternal(tcpData.getIpAddress(), outgoing, tcpData.getWaitForResponse());
    }

    private byte[] sendInternal(String ipAddress, byte[] outgoing, boolean waitForResponse) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            String tid = UUID.randomUUID().toString();

            if (waitForResponse) {
                return tcpSender.sendAndWaitForResponse(outgoing, inetAddress, Constants.PORT, tid);
            } else {
                tcpSender.send(outgoing, inetAddress, Constants.PORT, tid);
                return null; // or some default value for methods that don't return a response
            }
        } catch (UnknownHostException e) {
            log.error("Error getting host information.");
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            log.error("Error sending byte array.");
            throw new RuntimeException(e.getMessage());
        }
    }
}
