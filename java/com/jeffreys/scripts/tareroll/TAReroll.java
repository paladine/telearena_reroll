package com.jeffreys.scripts.tareroll;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Uninterruptibles;
import com.jeffreys.common.proto.Protos;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TAReroll {
  private static final Splitter SPACE_SPLITTER = Splitter.on(' ');
  private static final String YOU_MUST_REST = "You must rest a moment before proceeding!";

  private enum Statistic {
    INTELLECT,
    KNOWLEDGE,
    PHYSIQUE,
    STAMINA,
    AGILITY,
    CHARISMA,
    VITALITY;

    public String getDisplayString() {
      return String.format("%s:", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name()));
    }
  }

  private static ImmutableMap<String, Integer> createStatisticToIndexMap() {
    ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
    for (Statistic stat : Statistic.values()) {
      builder.put(stat.getDisplayString(), stat.ordinal());
    }

    return builder.build();
  }

  private static final ImmutableMap<String, Integer> STATISTIC_TO_INDEX =
      createStatisticToIndexMap();

  private final int wanted[] = new int[7];
  private final int stats[] = new int[wanted.length];
  private final RerollRequirements rerollRequirements;
  private final Scanner scanner;
  private final PrintWriter output;

  TAReroll(Scanner scanner, PrintWriter output, RerollRequirements rerollRequirements) {
    this.scanner = scanner;
    this.output = output;
    this.rerollRequirements = rerollRequirements;

    fillWantedArray(rerollRequirements);
  }

  public static void main(String[] args) {
    checkArgument(args.length > 0, "Need to pass a proto file as the first arg.");

    try {
      new TAReroll(
              new Scanner(System.in),
              new PrintWriter(System.out, /* autoflush= */ true),
              Protos.parseProtoFromTextFile(args[0], RerollRequirements.class))
          .run();
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  private enum Status {
    NO_MATCH,
    DONE;
  }

  private void fillWantedArray(RerollRequirements rerollRequirements) {
    wanted[Statistic.INTELLECT.ordinal()] = rerollRequirements.getIntellect();
    wanted[Statistic.KNOWLEDGE.ordinal()] = rerollRequirements.getKnowledge();
    wanted[Statistic.PHYSIQUE.ordinal()] = rerollRequirements.getPhysique();
    wanted[Statistic.STAMINA.ordinal()] = rerollRequirements.getStamina();
    wanted[Statistic.AGILITY.ordinal()] = rerollRequirements.getAgility();
    wanted[Statistic.CHARISMA.ordinal()] = rerollRequirements.getCharisma();
    wanted[Statistic.VITALITY.ordinal()] = rerollRequirements.getVitality();
  }

  private static int toInteger(String value) {
    return Integer.parseInt(Iterables.getFirst(SPACE_SPLITTER.split(value), "invalid"));
  }

  private Status reroll() {
    int statsFound = 0;

    output.print("reroll\r\n");
    output.flush();

    reroll_loop:
    while (true) {
      String line = scanner.nextLine();

      if (line.equals(YOU_MUST_REST)) {
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        statsFound = 0;
        output.print("reroll\r\n");
        output.flush();
        continue;
      }

      for (Map.Entry<String, Integer> entry : STATISTIC_TO_INDEX.entrySet()) {
        if (line.startsWith(entry.getKey())) {
          stats[entry.getValue()] = toInteger(line.substring(14));
          statsFound++;
          if (statsFound >= wanted.length) {
            break reroll_loop;
          }
        }
      }
    }

    for (int i = 0; i < wanted.length; ++i) {
      if (stats[i] < wanted[i]) {
        return Status.NO_MATCH;
      }
    }
    return Status.DONE;
  }

  void run() {
    System.err.printf(
        "Rolling for %s\r\n",
        Arrays.stream(wanted).mapToObj(Integer::toString).collect(Collectors.joining(",")));

    while (reroll() != Status.DONE) {
      ; // reroll again
    }

    output.printf("%s\r\n", rerollRequirements.getLogoffCommand());
    output.flush();
  }
}
