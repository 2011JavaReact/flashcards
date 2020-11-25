package com.revature.flashcards.service;

import com.revature.flashcards.dao.CardDao;
import com.revature.flashcards.dao.CardTemplateDao;
import com.revature.flashcards.exception.ServiceException;
import com.revature.flashcards.model.Auth;
import com.revature.flashcards.model.Card;
import com.revature.flashcards.model.CardInRequest;
import com.revature.flashcards.model.CardTemplate;
import com.revature.flashcards.response.APIError;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CardService extends BasicService<CardDao, Card, CardInRequest> {
  private final CardTemplateDao cardTemplateDao;

  public CardService() {
    this(new CardDao(), new CardTemplateDao());
  }

  public CardService(CardDao dao, CardTemplateDao cardTemplateDao) {
    super(dao);
    this.cardTemplateDao = cardTemplateDao;
  }

  public List<Card> getAllForUser(Auth auth, int userID)
      throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin && (userID != auth.userID)) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      ArrayList<Card> out = new ArrayList<>();
      Set<Integer> templateIdsForThisUser = new HashSet<>();

      cardTemplateDao.getAll().forEach(t -> {
        if (t.userID == userID) {
          templateIdsForThisUser.add(t.id);
        }
      });

      dao.getAll().forEach(o -> {
        if (templateIdsForThisUser.contains(o.templateID)) {
          out.add(new Card(o.templateID, o.data, o.id));
        }
      });

      return out;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Card> getAll(Auth auth) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      ArrayList<Card> out = new ArrayList<>();
      dao.getAll().forEach(o -> out.add(
          new Card(o.templateID, o.data, o.id)
      ));
      return out;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Card get(Auth auth, int id) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    }

    try {
      Card o = dao.get(id).orElseThrow(
          () -> new ServiceException(APIError.NOT_FOUND));

      if (!auth.admin && !hasTemplate(auth.userID, o.templateID)) {
        throw new ServiceException(APIError.FORBIDDEN);
      }

      return o;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Card update(Auth auth, CardInRequest o, int id)
      throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    }

    try {
      Card inDb = dao.get(id).orElseThrow(() ->
          new ServiceException(APIError.NOT_FOUND,
              "card to update doesnt exist!"));

      if (!auth.admin && !hasTemplate(auth.userID, inDb.templateID)) {
        throw new ServiceException(APIError.FORBIDDEN,
            "cannot modify a card for a different user!");
      }

      CardTemplate t = cardTemplateDao.get(o.templateID)
          .orElseThrow(() -> new ServiceException(APIError.BAD_REQUEST,
              "template for the card you want to make doesnt exist!"));

      if (!auth.admin && t.userID != auth.userID) {
        throw new ServiceException(APIError.FORBIDDEN,
            "cant use another user's template");
      }

      return dao.update(id, o);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Card create(Auth auth, CardInRequest o) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    }

    try {
      CardTemplate t = cardTemplateDao.get(o.templateID)
          .orElseThrow(() -> new ServiceException(APIError.BAD_REQUEST,
              "template for the card you want to make doesnt exist!"));

      if (!auth.admin && t.userID != auth.userID) {
        throw new ServiceException(APIError.FORBIDDEN, "cant use another " +
            "user's template");
      }

      return dao.create(o);
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
      Optional<Card> o = dao.get(id);
      if (o.isPresent()) {
        if (!auth.admin && !hasTemplate(auth.userID, o.get().templateID)) {
          throw new ServiceException(APIError.FORBIDDEN);
        }
        dao.delete(id);
      }
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  private boolean hasTemplate(int userId, int templateId) throws SQLException {
    List<CardTemplate> list = cardTemplateDao.getAll();

    for (int i = 0; i < list.size(); i++) {
      CardTemplate t = list.get(i);
      if (t.userID == userId && t.id == templateId) {
        return true;
      }
    }

    return false;
  }
}
