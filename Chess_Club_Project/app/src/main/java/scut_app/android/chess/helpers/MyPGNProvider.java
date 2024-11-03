package scut_app.android.chess.helpers;

import android.content.UriMatcher;
import android.net.Uri;

import scut_app.chess.PGNProvider;

public class MyPGNProvider extends PGNProvider {

    static {
        AUTHORITY = "scut.android.chess.helpers.MyPGNProvider";
        CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/games");

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "games", GAMES);
        sUriMatcher.addURI(AUTHORITY, "games/#", GAMES_ID);
    }
}
