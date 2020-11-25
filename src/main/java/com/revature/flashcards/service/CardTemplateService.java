package com.revature.flashcards.service;

import com.revature.flashcards.dao.CardTemplateDao;
import com.revature.flashcards.dao.UserDao;
import com.revature.flashcards.exception.ServiceException;
import com.revature.flashcards.model.Auth;
import com.revature.flashcards.model.CardTemplate;
import com.revature.flashcards.model.CardTemplateInRequest;
import com.revature.flashcards.response.APIError;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardTemplateService extends
    BasicService<CardTemplateDao, CardTemplate, CardTemplateInRequest> {
  private final UserDao userDao;

  public CardTemplateService() {
    this(new CardTemplateDao(), new UserDao());
  }

  public CardTemplateService(CardTemplateDao dao, UserDao userDao) {
    super(dao);
    this.userDao = userDao;
  }

  public List<CardTemplate> getAllForUser(Auth auth, int userID)
      throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin && (userID != auth.userID)) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      userDao.get(userID).orElseThrow(
          () -> new ServiceException(APIError.NOT_FOUND, "user not found"));

      ArrayList<CardTemplate> out = new ArrayList<>();
      dao.getAll().forEach(o -> {
        if (o.userID == userID) {
          out.add(
              new CardTemplate(o.userID, o.description, o.front, o.back, o.id));
        }
      });
      return out;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public List<CardTemplate> getAll(Auth auth) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      ArrayList<CardTemplate> out = new ArrayList<>();
      dao.getAll().forEach(o -> out.add(
          new CardTemplate(o.userID, o.description, o.front, o.back, o.id)
      ));
      return out;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public CardTemplate get(Auth auth, int id) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    }

    try {
      CardTemplate o = dao.get(id).orElseThrow(
          () -> new ServiceException(APIError.NOT_FOUND));

      if (!auth.admin && o.userID != auth.userID) {
        throw new ServiceException(APIError.FORBIDDEN);
      }

      return new CardTemplate(o.userID, o.description, o.front, o.back, o.id);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public CardTemplate update(Auth auth, CardTemplateInRequest o, int id)
      throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin && o.userID != auth.userID) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      CardTemplate inDb = dao.get(id).orElseThrow(() ->
          new ServiceException(APIError.NOT_FOUND, "card template doesnt exist")
      );

      if (inDb.userID != o.userID) {
        if (auth.admin) {
          throw new ServiceException(APIError.BAD_REQUEST,
              "cannot change the card templates owner.");
        }
        throw new ServiceException(APIError.FORBIDDEN,
            "cannot modify another user's card template!");
      }

      CardTemplate l = dao.update(id, o);
      return new CardTemplate(l.userID, l.description, l.front, l.back, l.id);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public CardTemplate create(Auth auth, CardTemplateInRequest o)
      throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin && (o.userID != auth.userID)) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      userDao.get(o.userID).orElseThrow(() ->
          new ServiceException(APIError.BAD_REQUEST,
              "user on the provided template doesnt exist"));

      CardTemplate t = dao.create(o);
      return new CardTemplate(t.userID, t.description, t.front, t.back, t.id);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(Auth auth, int id) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    }

    try {
      Optional<CardTemplate> o = dao.get(id);
      if (o.isPresent()) {
        if (!auth.admin && o.get().userID != auth.userID) {
          throw new ServiceException(APIError.FORBIDDEN);
        }
        dao.delete(id);
      }
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }
}
