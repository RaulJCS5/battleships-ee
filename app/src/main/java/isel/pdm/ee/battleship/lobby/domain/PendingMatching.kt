package isel.pdm.ee.battleship.lobby.domain


sealed class PendingMatching(val localPlayer: PlayerInfo, val matching: Matching)

class StartMatching(localPlayer: PlayerInfo, matching: Matching)
    : PendingMatching(localPlayer, matching)

class SentMatching(localPlayer: PlayerInfo, matching: Matching)
    : PendingMatching(localPlayer, matching)

