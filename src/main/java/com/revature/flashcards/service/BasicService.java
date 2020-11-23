package com.revature.flashcards.service;

import com.revature.flashcards.exception.ServiceException;
import com.revature.flashcards.model.Auth;
import java.util.List;

public abstract class BasicService<D, M, P> {
  protected D dao;

  public BasicService(D dao) {
    this.dao = dao;
  }

  public abstract List<M> getAll(Auth auth) throws ServiceException;

  public abstract M get(Auth auth, int id) throws ServiceException;

  public abstract M update(Auth auth, P o, int id) throws ServiceException;

  public abstract M create(Auth auth, P o) throws ServiceException;

  public abstract void delete(Auth auth, int id) throws ServiceException;
}
