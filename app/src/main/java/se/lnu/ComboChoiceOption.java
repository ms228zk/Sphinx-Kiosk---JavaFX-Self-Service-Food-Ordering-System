package se.lnu;

public class ComboChoiceOption {
  private final int id;
  private final String optionName;

  public ComboChoiceOption(int id, String optionName) {
    this.id = id;
    this.optionName = optionName;
  }

  public int getId() {
    return id;
  }

  public String getOptionName() {
    return optionName;
  }
}