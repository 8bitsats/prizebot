package me.y9san9.prizebot.resources.entities

import dev.inmo.tgbotapi.CommonAbstracts.TextSourcesList
import dev.inmo.tgbotapi.CommonAbstracts.plus
import dev.inmo.tgbotapi.types.MessageEntity.textsources.bold
import dev.inmo.tgbotapi.types.MessageEntity.textsources.italic
import dev.inmo.tgbotapi.types.MessageEntity.textsources.regular
import dev.inmo.tgbotapi.types.MessageEntity.textsources.underline
import me.y9san9.prizebot.database.giveaways_storage.ActiveGiveaway
import me.y9san9.prizebot.database.giveaways_storage.FinishedGiveaway
import me.y9san9.prizebot.database.giveaways_storage.Giveaway
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

    val title = bold(giveaway.title) + "\n\n"

    val untilTime = if(giveaway.raffleDate == null) listOf() else {
        val format = DateTimeFormatter.ofPattern (
            "d MMMM, HH:mm (XXX)", Locale.forLanguageTag(giveaway.languageCode)
        )
        val date = giveaway.raffleDate!!.format(format)
        underline(locale.raffleDate) + ": $date" + "\n"
    }

    val winnersCount = if(giveaway is ActiveGiveaway && giveaway.winnersCount.value > 1)
        underline(locale.winnersCount) + ": ${giveaway.winnersCount.value}\n"
    else listOf()

    val winner = if(giveaway is FinishedGiveaway) {
        val links = giveaway.winnerIds
            .map { id -> update.bot.getUserLink(id, locale.deletedUser) }

        regular("${locale.winner(plural = giveaway.winnerIds.size > 1)}: ") +
                links.flatMap { it + ", " }.dropLast(n = 1)
    } else listOf()

    val participateHint = if(giveaway is ActiveGiveaway)
        italic(locale.giveawayParticipateHint)
    else regular("")

    return title + winnersCount + untilTime + "\n" + winner + participateHint
}
