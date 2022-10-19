package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ModAddCommand;
import seedu.address.logic.commands.ModCommand;
import seedu.address.logic.commands.ModDeleteCommand;
import seedu.address.logic.commands.ModFindCommand;
import seedu.address.logic.commands.ModMarkCommand;
import seedu.address.logic.commands.ModUnmarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Mod;
import seedu.address.model.person.ModContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new ModCommand object.
 */
public class ModCommandParser implements Parser<ModCommand> {

    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Pattern INDEX_FORMAT = Pattern.compile("-?\\d+");

    /**
     * Parses the given {@code userInput} of arguments in the context of the ModCommand
     * and returns a ModCommand object for execution.
     *
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    @Override
    public ModCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(trimmedArgs);

        // check if empty and matches format
        if (trimmedArgs.isEmpty() || !matcher.matches()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModCommand.MESSAGE_USAGE));
        }

        // parse
        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // TODO: Add cases for other mod commands
        switch (commandWord) {
        case ModAddCommand.COMMAND_WORD:
            return parseAddCommand(arguments);
        case ModDeleteCommand.COMMAND_WORD:
            return parseDeleteCommand(arguments);
        case ModFindCommand.COMMAND_WORD:
            return parseFindCommand(arguments);
        case ModMarkCommand.COMMAND_WORD:
            return parseMarkCommand(arguments);
        case ModUnmarkCommand.COMMAND_WORD:
            return parseUnmarkCommand(arguments);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses a mod add command from user to construct a ModAddCommand for execution.
     *
     * @param args Arguments from user input.
     * @return A ModAddCommand for execution.
     * @throws ParseException If there is a parse error.
     */
    private ModAddCommand parseAddCommand(String args) throws ParseException {
        Index index;
        String trimmedArgs = args.trim();
        String indexFromCommand = getIndexFromCommand(trimmedArgs);
        Set<String> modsFromCommand = getModsFromCommand(trimmedArgs);
        Optional<ObservableList<Mod>> mods = parseMods(modsFromCommand);
        if (mods.isEmpty()) {
            throw new ParseException(ModCommand.MESSAGE_MODS_EMPTY);
        }

        try {
            index = ParserUtil.parseIndex(indexFromCommand);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModAddCommand.MESSAGE_USAGE), pe);
        }
        return new ModAddCommand(index, mods.get());
    }

    /**
     * Parses a mod delete command from user to construct a ModDeleteCommand for execution.
     *
     * @param args Arguments from user input.
     * @return A ModDeleteCommand for execution.
     * @throws ParseException If there is a parse error.
     */
    private ModDeleteCommand parseDeleteCommand(String args) throws ParseException {
        Index index;
        String trimmedArgs = args.trim();
        String indexFromCommand = getIndexFromCommand(trimmedArgs);
        Set<String> modsFromCommand = getModsFromCommand(trimmedArgs);
        Optional<ObservableList<Mod>> mods = parseMods(modsFromCommand);
        if (mods.isEmpty()) {
            throw new ParseException(ModCommand.MESSAGE_MODS_EMPTY);
        }

        try {
            index = ParserUtil.parseIndex(indexFromCommand);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModDeleteCommand.MESSAGE_USAGE), pe);
        }
        return new ModDeleteCommand(index, mods.get());
    }

    private ModFindCommand parseFindCommand(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(ModCommand.MESSAGE_MODS_EMPTY);
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new ModFindCommand(new ModContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

    /**
     * Parses a mod mark command from user to construct a ModMarkCommand for execution.
     *
     * @param args Arguments from user input.
     * @return A ModMarkCommand for execution.
     * @throws ParseException If there is a parse error.
     */
    private ModMarkCommand parseMarkCommand(String args) throws ParseException {
        Index index;
        String trimmedArgs = args.trim();
        String indexFromCommand = getIndexFromCommand(trimmedArgs);
        Set<String> modsFromCommand = getModsFromCommand(trimmedArgs);
        Optional<ObservableList<Mod>> mods = parseMods(modsFromCommand);
        if (mods.isEmpty()) {
            throw new ParseException(ModCommand.MESSAGE_MODS_EMPTY);
        }

        try {
            index = ParserUtil.parseIndex(indexFromCommand);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModMarkCommand.MESSAGE_USAGE), pe);
        }
        return new ModMarkCommand(index, mods.get());
    }

    /**
     * Parses a mod unmark command from user to construct a ModUnmarkCommand for execution.
     *
     * @param args Arguments from user input.
     * @return A ModUnmarkCommand for execution.
     * @throws ParseException If there is a parse error.
     */
    private ModUnmarkCommand parseUnmarkCommand(String args) throws ParseException {
        Index index;
        String trimmedArgs = args.trim();
        String indexFromCommand = getIndexFromCommand(trimmedArgs);
        Set<String> modsFromCommand = getModsFromCommand(trimmedArgs);
        Optional<ObservableList<Mod>> mods = parseMods(modsFromCommand);
        if (mods.isEmpty()) {
            throw new ParseException(ModCommand.MESSAGE_MODS_EMPTY);
        }

        try {
            index = ParserUtil.parseIndex(indexFromCommand);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModMarkCommand.MESSAGE_USAGE), pe);
        }
        return new ModUnmarkCommand(index, mods.get());
    }

    /**
     * Converts a collection of strings representing mod names to a set of mods.
     *
     * @param mods A collection of mods in string.
     * @return A set of mods.
     * @throws ParseException When mod names are of incorrect format.
     */
    private Optional<ObservableList<Mod>> parseMods(Collection<String> mods) throws ParseException {
        assert mods != null;

        if (mods.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> modSet = mods.size() == 1 && mods.contains("") ? Collections.emptySet() : mods;
        return Optional.of(ParserUtil.parseMods(modSet));
    }

    /**
     * Extracts out the index of the person specified in the user command.
     *
     * @param args The user command.
     * @return The index of the person in String.
     */
    private String getIndexFromCommand(String args) throws ParseException {
        String[] splittedArgs = args.split(" ");
        String index = splittedArgs[0];
        final Matcher matcher = INDEX_FORMAT.matcher(index.trim());
        if (!matcher.matches()) {
            throw new ParseException(ModCommand.MESSAGE_INDEX_EMPTY);
        }
        return index;
    }

    /**
     * Extracts out the mods specified in the user command.
     *
     * @param args The user command.
     * @return A set of mods of type String.
     */
    private Set<String> getModsFromCommand(String args) {
        String[] splittedArgs = args.split(" ");
        List<String> extractedMods = Arrays.asList(splittedArgs).subList(1, splittedArgs.length);
        extractedMods = extractedMods.stream().map(String::toUpperCase).collect(Collectors.toList());
        return new HashSet<>(extractedMods);
    }
}