package org.xin.watchservice;

import org.restlet.representation.Representation;

public interface DownloadClient {

  DownloadClient loginWith(String userName);

  void passWord(String password);

  Representation get();
}
