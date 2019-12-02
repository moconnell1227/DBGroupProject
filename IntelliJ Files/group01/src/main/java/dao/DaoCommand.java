package edu.calpoly.csc365.group01.dao;

public interface DaoCommand {
  Object execute(DaoManager daoManager);
}
