package scut_app.android.chess.ics;

import android.util.Log;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import scut_app.chess.board.ChessBoard;

public class ICSPatterns {
    private static final String TAG = "ICSPatterns";

    protected static final Pattern challenge = Pattern.compile("Challenge\\: (\\w+) \\((.+)\\) (\\w+) \\((.+)\\) (rated |unrated )(standard |blitz |wild |lightning )(\\d+) (\\d+)( \\(adjourned\\))?.*");


    protected static final Pattern storedRow = Pattern.compile("[\\s]*(\\d+)\\: (W|B) (\\w+)[\\s]*(Y|N).+");

    protected static final Pattern chat = Pattern.compile("(\\w+)(\\(\\w+\\))? tells you\\: (.+)");
    protected static final Pattern shouts = Pattern.compile("(\\w+)(\\(\\w+\\))? (c-)?shouts\\: (.+)");


    protected static final Pattern playerRow = Pattern.compile("(\\s+)?(.{4})([\\.\\:\\^\\ ])(\\w+)(\\(\\w+\\))?");

    protected static final Pattern sought = Pattern.compile("[\\s]*(\\d+)[\\s]+(\\d+|\\++|-+)[\\s]+([\\w\\(\\)]+)[\\s]+(\\d+)[\\s]+(\\d+)[\\s]+(rated|unrated?)[\\s]+([\\w/\\d]+?)[\\s]*(\\[white\\]|\\[black\\])?[\\s]*(\\d+)\\-(\\d+)[\\s]*([fm]+)?");


    protected static final Pattern gameRow = Pattern.compile("[\\s]*(\\d+) (\\d+) (\\w+)[\\s]+(\\d+) (\\w+)[\\s]+\\[ (s|b|l)(r|u)[\\s]*(\\d+)[\\s]*(\\d+)\\][\\s]*(\\d+):(\\d+)[\\s]*-[\\s]*(\\d+):(\\d+).+");

    protected static final Pattern loggingYouInAs = Pattern.compile("Logging you in as \"(\\w+)\"");
    protected static final Pattern returnToLoginAs = Pattern.compile("Press return to enter the server as \"(\\w+)\":");

    protected static final Pattern _pattGameInfo1 = Pattern.compile("\\{?\\w+\\s?\\d+?: (\\w+) \\((.{3,4})\\) (\\w+) \\((.{3,4})\\) (\\w+) (\\w+) (\\d+) (\\d+)");
    protected static final Pattern _pattGameInfo2 = Pattern.compile("\\w+: (\\w+) \\((.{3,4})\\) (\\w+) \\((.{3,4})\\) (\\w+) (\\w+) (\\d+) (\\d+)");
    protected static final Pattern _pattGameInfo3 = Pattern.compile("\\{\\w+\\s(\\d+) \\((\\w+) vs. (\\w+)\\) (.*)\\} (.*)");

    protected static final Pattern gameNumber = Pattern.compile("\\{Game (\\d+) .*");
    protected static final Pattern clock = Pattern.compile("\\((\\d+):(\\d+)\\)");

    protected static final Pattern pattEndGame = Pattern.compile("(\\w+) \\((\\w+)\\) vs. (\\w+) \\((\\w+)\\) --- \\w+ (\\w+\\s+\\d{1,2}, )\\w.*(\\d{4})\\s+(\\w.+), initial time: (\\d{1,3}) minutes, increment: (\\d{1,3})(.|\\n)*\\{(.*)\\}");

    public static final String EMPTY = "";
    protected static final String loginChars = "\r\n\uefbf\ubdef\ubfbd\uefbf\ubdef\ubfbd\ud89e\u0001\ufffd\ufffd";

    public boolean containsGamesDisplayed(String buffer, int lineCount) {
        return lineCount > 3 && buffer.indexOf("\\") == -1 && buffer.indexOf("games displayed") >= 0;
    }

    public boolean containsAdsDisplayed(String buffer, int lineCount) {
        return lineCount > 2 && buffer.indexOf("\\") == -1 && buffer.indexOf("ads displayed.") >= 0;
    }

    public boolean containsPlayersDisplayed(String buffer, int lineCount) {
        return lineCount > 3 && buffer.indexOf("\\") == -1 && buffer.indexOf("players displayed (of ") > 0;
    }

    public Matcher gameHistoryMatcher(String buffer, int lineCount) {
        if (lineCount > 3) {
            Matcher ret = pattEndGame.matcher(buffer);
            if (ret.find()) {
                Log.d(TAG, "MATCHES");
                return ret;
            }
        }
        return null;
    }

    public boolean isInvalidPassword(String buffer) {
        return buffer.contains("**** Invalid");
    }

    public boolean isSessionStarting(String buffer) {
        return buffer.contains("**** Starting ");
    }

    public String parseGuestHandle(String buffer) {
        Matcher match = returnToLoginAs.matcher(buffer);
        if (match.find()) {
            return match.group(1);
        }
        return null;
    }

    public HashMap<String, String> parseGameLine(String line) {
        Matcher match = gameRow.matcher(line);
        if (match.matches()) {
            HashMap<String, String> item = new HashMap<String, String>();
            //  93 2036 WFMKierzek  2229 FMKarl     [ su120   0]  26:13 -  3:49 (22-22) W: 28
            item.put("nr", match.group(1));
            item.put("text_rating1", match.group(2));
            item.put("text_name1", match.group(3));
            item.put("text_rating2", match.group(4));
            item.put("text_name2", match.group(5));
            item.put("text_type", match.group(6).toUpperCase() + match.group(7).toUpperCase());
            item.put("text_time1", match.group(10) + ":" + match.group(11));
            item.put("text_time2", match.group(12) + ":" + match.group(13));

            return item;
        }
        return null;
    }

    public HashMap<String, String> parsePlayerLine(String line) {
        Matcher match = playerRow.matcher(line);
        if (match.find()) {
            String name = match.group(4);
            if (name != null && match.group(2) != null) {
                String code = match.group(5);
                if (code == null) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("text_name", name);
                    item.put("text_rating", match.group(2));
                    return item;
                } else if (code.equals("(U)") || code.equals("(FM)") || code.equals("(GM)") ||
                            code.equals("(IM)") || code.equals("(WIM)") || code.equals("(WGM)")) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    name += code;
                    item.put("text_name", name);
                    item.put("text_rating", match.group(2));
                    return item;
                }
            }
        }
        return null;
    }

    public HashMap<String, String> parseGameInfo(String line) {
        if (line.contains("Game") || line.contains("Creating:") || line.contains("Issuing:") || line.contains("Challenge:")) {
            Matcher mat = _pattGameInfo1.matcher(line);
            Matcher mat2 = _pattGameInfo2.matcher(line);

            if (mat.matches() || mat2.matches()) {  //mat and mat2 are the beginning game info
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("whiteHandle", mat.matches() ? mat.group(1) : mat2.group(1));
                String rating = mat.matches() ? mat.group(2) : mat2.group(2);
                if (rating.equals("++++")) {
                    rating = "UNR";
                }
                item.put("whiteRating", rating);
                item.put("blackHandle", mat.matches() ? mat.group(3) : mat2.group(3));
                rating = mat.matches() ? mat.group(4) : mat2.group(4);
                if (rating.equals("++++")) {
                    rating = "UNR";
                }
                item.put("blackRating", rating);
                return item;
            }
        }
        return null;
    }

    public HashMap<String, String> parseBoard(String line) {
        if (line.indexOf("<12> ") >= 0) {
            // this can be multiple lines!
            String[] gameLines = line.split("<12> ");
            HashMap<String, String> item = new HashMap<String, String>();

            if (gameLines[1].contains("none (0:00) none")) {
                item.put("FEN", gameLines[1]);
            }

            for (int j = 0; j < gameLines.length; j++) {
                // at least 65 chars
                if (gameLines[j].length() > 65) {
                    item.put("board", gameLines[j]);
                    break;
                }
            }
            return item;
        }
        return null;
    }

    public boolean isGameInfoEnd(String line) {
        Matcher matcher = _pattGameInfo3.matcher(line);
        return matcher.matches();
    }

    public HashMap<String, String> parseChallenge(String line, String handle) {
        if (line.indexOf("Challenge:") >= 0) {
            Matcher match = challenge.matcher(line);
            if (match.matches()) {
                String opponent, rating;
                if (match.group(1).equals(handle)) {
                    opponent = match.group(3);
                    rating = match.group(4);
                } else {
                    opponent = match.group(1);
                    rating = match.group(2);
                }
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("opponent", opponent);
                item.put("rating", rating);
                item.put("minutes", match.group(7));
                item.put("seconds", match.group(8));
                item.put("type", match.group(5));
                item.put("num", match.group(6));
                return item;
            }
        }
        return null;
    }

    public int getCreatingOrContinuingGameNumber(String line) {
        if(line.indexOf("{Game ") >= 0 && (line.indexOf(" Creating ") > 0 || line.indexOf(" Continuing ") > 0)) {
            Matcher m = gameNumber.matcher(line);
            if (m.matches()) {
                try {
                    return Integer.parseInt(m.group(1));
                } catch (Exception ex) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public boolean isResumingAdjournedGame(String line) {
        return line.indexOf("Creating: ") >= 0 && line.indexOf("(adjourned)") >= 0;
    }

    public boolean isIllegalMove(String line) {
        return line.indexOf("Illegal move (") == 0;
    }

    public boolean isSeekNotAvailable(String line) {
        return line.equals("That seek is not available.");
    }

    public boolean isAbortRequest(String line, String opponent) {
        return !line.contains("\\") && line.indexOf(opponent + " would like to abort the game") >= 0;
    }

    public boolean isAbortedConfirmed(String line) {
        return !line.contains("\\") && line.indexOf("Game aborted by mutual agreement}") >= 0;
    }

    public boolean isDrawConfirmed(String line) {
        return !line.contains("\\") && line.indexOf("Game drawn by mutual agreement}") >= 0;
    }

    public boolean isAdjournRequest(String line, String opponent) {
        return !line.contains("\\") && line.contains(opponent + " would like to adjourn the game; type \"adjourn\" to accept.");
    }

    public boolean isDrawRequest(String line, String opponent) {
        return !line.contains("\\") && line.contains(opponent + " offers you a draw.");
    }

    public boolean isTakeBackRequest(String line, String opponent) {
        return !line.contains("\\") && line.contains(opponent + " would like to take back ");
    }

    public boolean isAbortedOrAdourned(String line) {
        return line.indexOf("{Game " /*+ getGameNum()*/) >= 0 && line.indexOf("} *") > 0;
    }

    public int gameState(String line) {
        if (line.indexOf("{Game " /*+ getGameNum()*/) >= 0) {
            if (line.contains(" resigns} ")) {
                return line.contains("} 1-0") ? ChessBoard.BLACK_RESIGNED : ChessBoard.WHITE_RESIGNED;
            } else if (line.contains("forfeits on time")) {
                return line.contains("} 1-0") ? ChessBoard.BLACK_FORFEIT_TIME : ChessBoard.WHITE_FORFEIT_TIME;
            } else if (line.contains("checkmated")) {
                return ChessBoard.MATE;
            }
        } else if (line.contains("} 1/2-1/2")) {
            if (line.contains("Game drawn by mutual agreement}")) {
                return ChessBoard.DRAW_AGREEMENT;
            } else if (line.contains("material}")) {
                return ChessBoard.DRAW_MATERIAL;
            } else {
                return ChessBoard.DRAW_50;
            }
        }

        return ChessBoard.PLAY;
    }

    public boolean isAbortOrDrawOrAdjourneRequestSent(String line) {
        return line.equals("Draw request sent.") || line.equals("Abort request sent.") || line.equals("Takeback request sent.");
    }

    public boolean isNowOservingGame(String line) {
        return line.indexOf("You are now observing game") >= 0;
    }

    public boolean isStopObservingGame(String line) {
        return line.indexOf("Removing game") >= 0 && line.indexOf("from observation list") > 0;
    }

    public boolean isStopExaminingGame(String line) {
        return line.indexOf("You are no longer examining game") >= 0;
    }

    public boolean isPuzzleStarted(String line) {
        return line.indexOf("puzzlebot has made you an examiner of game") >= 0;
    }

    public boolean isPuzzleStopped(String line) {
        return line.indexOf("Your current problem has been stopped") >= 0;
    }

    public boolean isPuzzleSolved(String line) {
        return line.indexOf("You solved problem number ") >= 0;
    }

    public HashMap<String, String> parseSought(String line) {
        Matcher match = sought.matcher(line);
        if (match.matches()) {
            if (match.groupCount() > 7) {

                String s, type = match.group(7), rated = match.group(6);
                // 1   2    3                  4   5  6       7          8
                // 209 1739 rahulso            15  10 unrated standard   [white]     0-9999 m
                s = String.format("%2dm+%2ds", Integer.parseInt(match.group(4)), Integer.parseInt(match.group(5)));

                if (type.indexOf("blitz") >= 0 || type.indexOf("standard") >= 0) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    if (type.indexOf("standard") >= 0) {
                        type = "";
                    }
                    item.put("text_game", s + " " + rated + " " + type);
                    item.put("play", match.group(1));
                    item.put("text_name", match.group(3));
                    item.put("text_rating", match.group(2));

                    return item;
                }
            }
        }
        return null;
    }

    public HashMap<String, String> parseGameRow(String line) {
        Matcher match = gameRow.matcher(line);
        if (match.matches()) {
            HashMap<String, String> item = new HashMap<String, String>();
            //  93 2036 WFMKierzek  2229 FMKarl     [ su120   0]  26:13 -  3:49 (22-22) W: 28
            item.put("nr", match.group(1));
            item.put("text_rating1", match.group(2));
            item.put("text_name1", match.group(3));
            item.put("text_rating2", match.group(4));
            item.put("text_name2", match.group(5));
            item.put("text_type", match.group(6).toUpperCase() + match.group(7).toUpperCase());
            item.put("text_time1", match.group(10) + ":" + match.group(11));
            item.put("text_time2", match.group(12) + ":" + match.group(13));
            return item;
        }
        return null;
    }

    public HashMap<String, String> parseStoredRow(String line) {
        Matcher match = storedRow.matcher(line);
        if (match.matches()) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("nr_stored", match.group(1));
            item.put("color_stored", match.group(2));
            item.put("text_name_stored", match.group(3));
            item.put("available_stored", match.group(4).equals("Y") ? "*" : "");
            return item;
        }
        return null;
    }

    public String parseGameHistory(String sEnd, Matcher _matgame) {
        sEnd = sEnd.trim().replaceAll(" +", " ");
        sEnd = sEnd.replaceAll("\\{.*\\}", "");

        String site = "FICS";
        String _FEN1, _FEN2;

        String sMoves = sEnd.substring(sEnd.indexOf("1."), sEnd.length());

        sMoves = sMoves.replaceAll("\\s*\\([^\\)]*\\)\\s*", " ");


        return sMoves;
    }

    public boolean filterLine(String line) {
        return line.length() < 3 || line.contains("seeking");
    }
    public boolean filterBuffer(String buffer) {
        Matcher matchChat = chat.matcher(buffer);
        if (matchChat.find()) {
            Log.d(TAG, "Filter chat " + matchChat.group(0));
            return true;
        }
        Matcher matchShout = shouts.matcher(buffer);
        if (matchShout.find()) {
            Log.d(TAG, "Filter shout " + matchShout.group(0));
            return true;
        }

        return false;
    }

    public static String replaceChars(final String str, final String searchChars, String replaceChars) {
        if (isEmpty(str) || isEmpty(searchChars)) {
            return str;
        }
        if (replaceChars == null) {
            replaceChars = EMPTY;
        }
        boolean modified = false;
        final int replaceCharsLength = replaceChars.length();
        final int strLength = str.length();
        final StringBuilder buf = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            final char ch = str.charAt(i);
            final int index = searchChars.indexOf(ch);
            if (index >= 0) {
                modified = true;
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
            } else {
                buf.append(ch);
            }
        }
        if (modified) {
            return buf.toString();
        }
        return str;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    private String convertSecondsToClock(int time1) {
        String clock, timeString;
        int hours, minutes, seconds;
        hours = time1 / 3600;
        minutes = (time1 % 3600) / 60;
        seconds = time1 % 60;

        timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        clock = "{[%clk " + timeString + "]}";

        return clock;
    }
}
