package me.y9san9.prizebot.resources.entities

import dev.inmo.tgbotapi.CommonAbstracts.TextSourcesList
import dev.inmo.tgbotapi.CommonAbstracts.plus
import dev.inmo.tgbotapi.types.MessageEntity.textsources.*
import me.y9san9.prizebot.database.giveaways_storage.ActiveGiveaway
import me.y9san9.prizebot.database.giveaways_storage.FinishedGiveaway
import me.y9san9.prizebot.database.giveaways_storage.Giveaway
import me.y9san9.prizebot.database.giveaways_storage.conditions_storage.Condition
import me.y9san9.prizebot.database.giveaways_storage.locale
import me.y9san9.telegram.updates.primitives.BotUpdate
import me.y9san9.telegram.extensions.telegram_bot.getUserLink
import java.time.format.DateTimeFormatter
import java.util.*


suspend fun giveawayEntities (
    update: BotUpdate,
    giveaway: Giveaway
): TextSourcesList {
    val locale = giveaway.locale

    val title = bold(giveaway.title) + ""

    val untilTime = if(giveaway.raffleDate == null) listOf() else {
        val format = DateTimeFormatter.ofPattern (
            "d MMMM, HH:mm (XXX)", Locale.forLanguageTag(giveaway.languageCode)
        )
        val date = giveaway.raffleDate!!.format(format)
        bold(locale.raffleDate) + ": $date" + ""
    }

    val winnersCount = if(giveaway is ActiveGiveaway && giveaway.winnersCount.value > 1)
        bold(locale.winnersCount) + ": ${giveaway.winnersCount.value}"
    else listOf()

    val conditions = giveaway.conditions.list
    val conditionsEntities = if(conditions.isNotEmpty()) {
        bold(locale.giveawayConditions) + "\n" +
                conditions
                    .sortedBy { if(it is Condition.Invitations) 0 else 1 }
                    .flatMap { condition ->
                        when(condition) {
                            is Condition.Subscription ->
                                regular("• ") + locale.subscribeToChannel(condition.channelUsername)
                            is Condition.Invitations ->
                                regular("• ") + locale.inviteFriends(condition.count.int)
                        } + "\n"
                    }.dropLast(n = 1)
    } else listOf()

    val winner = if(giveaway is FinishedGiveaway) {
        val links = giveaway.winnerIds
            .map { id -> update.bot.getUserLink(id, locale.deletedUser) }
            .flatMap { it + ", " }
            .dropLast(n = 1)

        regular("${locale.winner(plural = giveaway.winnerIds.size > 1)}: ") + links
    } else listOf()

    val participateHint = if(giveaway is ActiveGiveaway)
        italic(locale.giveawayParticipateHint) + ""
    else listOf()

    return listOf (
        title, winnersCount, untilTime, conditionsEntities, winner, participateHint
    ).flatMap { if(it.isEmpty()) it else it + "\n\n" }
        .dropLast(n = 1)
}
