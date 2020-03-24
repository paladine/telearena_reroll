package com.jeffreys.scripts.tareroll;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TARerollTest {

  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final PrintWriter output = new PrintWriter(outputStream);

  @Test
  public void simpleMatch() {
    RerollRequirements requirements = createRerollRequirements(0, 0, 0, 0, 0, 0, 0);
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(createNormalValues(0, 0, 0, 0, 0, 0, 0).getBytes());

    new TAReroll(new Scanner(inputStream), output, requirements).run();

    assertThat(outputStream.toString()).isEqualTo("reroll\r\n=x\r\n");
  }

  @Test
  public void notFoundAtFirst() {
    RerollRequirements requirements = createRerollRequirements(20, 20, 20, 20, 20, 20, 20);
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(
            (createNormalValues(0, 0, 0, 0, 0, 0, 0)
                    + createNormalValues(20, 20, 20, 20, 20, 20, 20))
                .getBytes());

    new TAReroll(new Scanner(inputStream), output, requirements).run();

    assertThat(outputStream.toString()).isEqualTo("reroll\r\nreroll\r\n=x\r\n");
  }

  @Test
  public void simpleMatch_arcticZone() {
    RerollRequirements requirements = createRerollRequirements(0, 0, 0, 0, 0, 0, 0);
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(createArcticZoneValues(0, 0, 0, 0, 0, 0, 0).getBytes());

    new TAReroll(new Scanner(inputStream), output, requirements).run();

    assertThat(outputStream.toString()).isEqualTo("reroll\r\n=x\r\n");
  }

  @Test
  public void notFoundAtFirst_arcticZone() {
    RerollRequirements requirements = createRerollRequirements(20, 20, 20, 20, 20, 20, 20);
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(
            (createArcticZoneValues(0, 0, 0, 0, 0, 0, 0)
                    + createArcticZoneValues(20, 20, 20, 20, 20, 20, 20))
                .getBytes());

    new TAReroll(new Scanner(inputStream), output, requirements).run();

    assertThat(outputStream.toString()).isEqualTo("reroll\r\nreroll\r\n=x\r\n");
  }

  private static String createNormalValues(
      int intellect,
      int knowledge,
      int physique,
      int stamina,
      int agility,
      int charisma,
      int vitality) {
    return String.format(
        "Intellect:    %d\r\n"
            + "Knowledge:    %d\r\n"
            + "Physique:     %d\r\n"
            + "Stamina:      %d\r\n"
            + "Agility:      %d\r\n"
            + "Charisma:     %d\r\n\r\n"
            + "Vitality:     %d / %d\r\n",
        intellect, knowledge, physique, stamina, agility, charisma, vitality, vitality);
  }

  /*
  Intellect:    13    Max: 17
  Knowledge:    10    Max: 17
  Physique:     16    Max: 26
  Stamina:      28    Max: 29
  Agility:      9     Max: 14
  Charisma:     11    Max: 14

  Vitality:     20    Max: 30

  Gold:         128   Max: 268
  */
  private static String createArcticZoneValues(
      int intellect,
      int knowledge,
      int physique,
      int stamina,
      int agility,
      int charisma,
      int vitality) {
    return String.format(
        "Intellect:    %d    Max: 99\r\n"
            + "Knowledge:    %d    Max: 99\r\n"
            + "Physique:     %d    Max: 99\r\n"
            + "Stamina:      %d    Max: 99\r\n"
            + "Agility:      %d    Max: 99\r\n"
            + "Charisma:     %d    Max: 99\r\n\r\n"
            + "Vitality:     %d    Max: 99\r\n\r\n",
        intellect, knowledge, physique, stamina, agility, charisma, vitality);
  }

  private static RerollRequirements createRerollRequirements(int... values) {
    return RerollRequirements.newBuilder()
      .setIntellect(values[0])
      .setKnowledge(values[1])
      .setPhysique(values[2])
      .setStamina(values[3])
      .setAgility(values[4])
      .setCharisma(values[5])
      .setVitality(values[6])
      .setLogoffCommand("=x")
      .build();
  }
}
