package dao;

public interface DaoCommand {
  Object execute(DaoManager daoManager);
}
