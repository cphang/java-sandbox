package org.xin.watchservice;

import org.restlet.data.ChallengeRequest;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class DownloadClientImpl implements DownloadClient {

  private final ClientResource clientResource;
  private String password;
  private String loginName;
  private ChallengeRequest cr;

  public DownloadClientImpl(String downloadUri) {
    clientResource = new ClientResource(downloadUri);
  }

  @Override
  public DownloadClient loginWith(String loginName) {
    this.loginName = loginName;
    return this;
  }

  @Override
  public void passWord(String password) {
    this.password = password;
  }

  @Override
  public Representation get() {
    try {
      return clientResource.get();
    } catch (final ResourceException e) {
      if (needsToAuthentificate() && usingHttpDigest()) {
        login();
        return clientResource.get();
      }
    }
    return clientResource.get();
  }

  private boolean usingHttpDigest() {
    for (final ChallengeRequest cr : clientResource.getChallengeRequests()) {
      if (isHttpDigest(cr)) {
        this.cr = cr;
        return true;
      }
    }
    return false;
  }

  private boolean needsToAuthentificate() {
    return clientResource.getStatus().equals(Status.CLIENT_ERROR_UNAUTHORIZED);
  }

  private void login() {
    clientResource.setChallengeResponse(new ChallengeResponse(cr,
        clientResource.getResponse(), loginName, password.toCharArray()));
  }

  private boolean isHttpDigest(final ChallengeRequest cr) {
    return cr.getScheme().equals(ChallengeScheme.HTTP_DIGEST);
  }

}
