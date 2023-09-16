package isel.pdm.ee.battleship.lobby.domain

/**
 * Sum type used to represent pending match events.
 * [StartMatching] means that a match is about to start because a remote player matched
 * the local player
 * [SentMatching] means that a match is about to start because the local player matched
 * another player in the lobby
 */

sealed class PendingMatching(val localPlayer: PlayerInfo, val matching: Matching)

class StartMatching(localPlayer: PlayerInfo, matching: Matching)
    : PendingMatching(localPlayer, matching)

class SentMatching(localPlayer: PlayerInfo, matching: Matching)
    : PendingMatching(localPlayer, matching)

