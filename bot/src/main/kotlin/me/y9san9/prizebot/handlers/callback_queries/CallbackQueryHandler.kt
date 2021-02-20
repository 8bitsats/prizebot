package me.y9san9.prizebot.handlers.callback_queries

import me.y9san9.prizebot.handlers.callback_queries.command.*
import me.y9san9.prizebot.models.telegram.PrizebotCallbackQueryUpdate
import me.y9san9.prizebot.resources.CALLBACK_ACTION_DELETE_GIVEAWAY
import me.y9san9.prizebot.resources.CALLBACK_ACTION_PARTICIPATE
import me.y9san9.prizebot.resources.CALLBACK_ACTION_RAFFLE_GIVEAWAY
import me.y9san9.prizebot.resources.CALLBACK_ACTION_SELF_GIVEAWAYS_CONTROL
import me.y9san9.telegram.updates.extensions.command.commandOrAnswer


object CallbackQueryHandler {
    suspend fun handle(update: PrizebotCallbackQueryUpdate) = update.commandOrAnswer(splitter = "_") {
        case("$CALLBACK_ACTION_PARTICIPATE", argsCount = 1) {
            ParticipateCommand.handle(update)
        }
        case("$CALLBACK_ACTION_SELF_GIVEAWAYS_CONTROL", argsCount = 1) {
            SelfGiveawaysSendCommand.handle(update)
        }
        case("$CALLBACK_ACTION_SELF_GIVEAWAYS_CONTROL", argsCount = 2) {
            SelfGiveawaysButtonsCommand.handle(update)
        }
        case("$CALLBACK_ACTION_DELETE_GIVEAWAY", argsCount = 1) {
            DeleteGiveawayCommand.handle(update)
        }
        case("$CALLBACK_ACTION_RAFFLE_GIVEAWAY", argsCount = 1) {
            RaffleCommand.handle(update)
        }
    }
}
