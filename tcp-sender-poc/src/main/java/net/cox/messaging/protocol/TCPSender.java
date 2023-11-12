/*****************************************************************************
*                                                                            *
*               Copyright (c) 2012: Cox Communications, Inc.                 *
*            P R O P R I E T A R Y & C O N F I D E N T I A L                 *
*                                                                            *
*    Cox Communications reserves all rights in this Program as delivered.    *
*    This Program or any portion thereof may not be reproduced in any form   *
*    whatsoever except as provided by license from Cox Communications, Inc.  *
*    Use of this material without the express written consent of             *
*    Cox Communications, Inc. shall be an infringement of copyright and      *
*    any other intellectual property rights that may be incorporated into    *
*    this Program.                                                           *
*                                                                            *
*****************************************************************************/
package net.cox.messaging.protocol;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

@Slf4j
@Component
public class TCPSender {

    public static final int TIMEOUT = 5000;

    private int m_connect = 5000;

    /**
     * Sends a TCP message and returns without waiting for a response
     * 
     * @param outgoing
     *        bytes to send
     * @param ipAddress
     *        destination IP
     * @param port
     *        destination port
     * @throws IOException
     *         if there was an error connecting to the specified port or writing
     *         to the socket
     */
    public void send(byte[] outgoing, InetAddress ipAddress, int port, final String tid) throws IOException {
        if ( !canCommunicateToBoxes() ) {
           log.info("Skipping send due to configuration ("+tid+")|ip address: " + ipAddress.getHostAddress() + "|port: " + port + "|bytes: " + outgoing.length);
           return;
        }
        log.debug("sending ("+tid+")|ip address: " + ipAddress.getHostAddress() + "|port: " + port + "|bytes: " + outgoing.length);
        Socket socket = new Socket();
        socket.setSoTimeout(TIMEOUT);

        InetSocketAddress address = new InetSocketAddress(ipAddress, port);

        try {
            socket.connect(address, m_connect);
        } catch (SocketTimeoutException ex) {
           if ( port == 1024 ) {
            log.error("Connection to " + address + " timed out. Maybe the set top is down?");
           } else {
            log.error("Connection to " + address + " timed out. Maybe the server is down?");
           }
            throw ex;
        }
        try {
            final OutputStream os = socket.getOutputStream();
            os.write(outgoing);
            os.flush();
        } finally {
            socket.close();
        }

        log.debug("sent ("+tid+")|ip address: " + ipAddress.getHostAddress() + "|port: " + port + "|bytes: " + outgoing.length);
    }

    /**
     * Sends a TCP message and waits for a response. Returns the response bytes
     * 
     * @param outgoing
     *        bytes to send
     * @param ipAddress
     *        destination IP
     * @param port
     *        destination port
     * @throws IOException
     *         if there was an error connecting to the specified port or writing
     *         to the socket
     * @return response bytes
     */
    public byte[] sendAndWaitForResponse(byte[] outgoing, InetAddress ipAddress, int port, final String tid) throws IOException {
        if ( !canCommunicateToBoxes() ) {
           log.info("Skipping send and wait due to configuration ("+tid+")|ip address: " + ipAddress.getHostAddress() + "|port: " + port + "|bytes: " + outgoing.length);
           return null;
        }
        log.debug("send and wait ("+tid+")|ip address: " + ipAddress.getHostAddress() + "|port: " + port + "|bytes: "
                + outgoing.length);
        Socket socket = new Socket();
        socket.setSoTimeout(TIMEOUT);

        InetSocketAddress address = new InetSocketAddress(ipAddress, port);

        try {
            socket.connect(address, m_connect);
        } catch (SocketTimeoutException ex) {
            log.error("Connection to " + address + " timed out.");
            throw ex;
        }

        DataInputStream input = new DataInputStream(socket.getInputStream());

        final OutputStream os = socket.getOutputStream();
        os.write(outgoing);
        os.flush();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte nextByte;
        log.trace("About to start reading from the inputStream");
        try {
            nextByte = input.readByte();
            baos.write(nextByte);
            while (input.available() > 0) {
                nextByte = input.readByte();
                log.trace("send and wait|read byte: " + nextByte);
                baos.write(nextByte);
            }
            baos.close();
        } catch (EOFException ex) {
            log.trace("Socket was closed");
        } catch (SocketTimeoutException ex) {
            log.debug("Socket timed out.");
        } finally {
            log.trace("Closing socket in finally block.");
            socket.close();
        }
        byte[] returnBytes = baos.toByteArray();
        log.trace("send and wait ("+tid+")|received bytes: " + returnBytes.length);
        return returnBytes;
    }

    public static boolean canCommunicateToBoxes() {
        return Boolean.TRUE;
    }
}
