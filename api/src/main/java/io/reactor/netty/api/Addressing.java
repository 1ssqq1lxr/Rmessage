package io.reactor.netty.api;

import reactor.core.Exceptions;

import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


public final class Addressing {

  private Addressing() {

  }


  public static InetAddress getLocalIpAddress(
      String listenAddress, String listenInterface, boolean preferIPv6) {
    InetAddress ipAddress;
    if (isNotEmpty(listenAddress) && isNotEmpty(listenInterface)) {
      throw new IllegalArgumentException(
          "Not allowed to set both listenAddress and listenInterface, choose one");
    } else if (isNotEmpty(listenAddress)) {
      try {
        ipAddress = InetAddress.getByName(listenAddress);
      } catch (UnknownHostException e) {
        throw new IllegalArgumentException("Unknown listenAddress: " + listenAddress);
      }
      // account that 0.0.0.0 is not allowed
      if (ipAddress.isAnyLocalAddress()) {
        throw new IllegalArgumentException(
            "listenAddress: " + listenAddress + " cannot be a wildcard address");
      }
      // ensure address is valid
      if (!isValidLocalIpAddress(ipAddress)) {
        throw new IllegalArgumentException(
            "listenAddress: " + listenAddress + " doesn't belong to any active network interface");
      }
    } else if (isNotEmpty(listenInterface)) {
      ipAddress = getNetworkInterfaceIpAddress(listenInterface, preferIPv6);
    } else {
      // fallback to local ip address
      ipAddress = getLocalIpAddress();
    }
    return ipAddress;
  }


  public static InetAddress getLocalIpAddress() {
    try {
      return InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      throw Exceptions.propagate(e);
    }
  }


  private static InetAddress getNetworkInterfaceIpAddress(
      String listenInterface, boolean preferIPv6) {
    try {
      NetworkInterface ni = NetworkInterface.getByName(listenInterface);
      if (ni == null) {
        throw new IllegalArgumentException(
            "Configured network interface: " + listenInterface + " could not be found");
      }
      if (!ni.isUp()) {
        throw new IllegalArgumentException(
            "Configured network interface: " + listenInterface + " is not active");
      }
      Enumeration<InetAddress> addrs = ni.getInetAddresses();
      if (!addrs.hasMoreElements()) {
        throw new IllegalArgumentException(
            "Configured network interface: "
                + listenInterface
                + " was found, but had no addresses");
      }
      // try to return the first address of the preferred type, otherwise return the first address
      InetAddress result = null;
      while (addrs.hasMoreElements()) {
        InetAddress addr = addrs.nextElement();
        if (preferIPv6 && addr instanceof Inet6Address) {
          return addr;
        }
        if (!preferIPv6 && addr instanceof Inet4Address) {
          return addr;
        }
        if (result == null) {
          result = addr;
        }
      }
      return result;
    } catch (SocketException e) {
      throw new IllegalArgumentException(
          "Configured network interface: " + listenInterface + " caused an exception", e);
    }
  }


  private static boolean isValidLocalIpAddress(InetAddress listenAddress)
      throws IllegalArgumentException {
    List<NetworkInterface> networkInterfaces;
    try {
      networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
    } catch (SocketException e) {
      throw new IllegalArgumentException("Can't get list of network interfaces", e);
    }
    // go and check out network interface by IP address
    for (NetworkInterface ni : networkInterfaces) {
      try {
        if (ni.isUp()) {
          Enumeration<InetAddress> addrs = ni.getInetAddresses();
          while (addrs.hasMoreElements()) {
            InetAddress addr = addrs.nextElement();
            if (addr.getHostAddress().equals(listenAddress.getHostAddress())) {
              return true;
            }
          }
        }
      } catch (SocketException e) {
        throw new IllegalArgumentException("Network interface: " + ni + " caused an exception", e);
      }
    }
    // looked at all network interfaces and didn't match IP address
    return false;
  }

  private static boolean isNotEmpty(String string) {
    return string != null && string.length() > 0;
  }
}
