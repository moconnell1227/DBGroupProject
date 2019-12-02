package Models;

public class Customer {
  private Integer id;
  private String firstname;
  private String lastname;

  public Customer() {
    this.id = null;
    this.firstname = null;
    this.lastname = null;
  }

  public Customer(String fn, String ln) {
    this.id = null;
    this.firstname = fn;
    this.lastname = ln;
  }

  public Customer(Integer id, String fn, String ln) {
    this.id = id;
    this.firstname = fn;
    this.lastname = ln;
  }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getFirstName() { return firstname; }
  public String getLastName() { return lastname; }
  public void setFirstName(String fn) { this.firstname = fn; }
  public void setLastName(String ln) { this.lastname = ln; }

  @Override
  public String toString() {
    return "cID: " + id.toString() + ", First Name: " + firstname + ", Last Name: " + lastname;
  }

}
