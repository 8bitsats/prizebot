package me.y9san9.prizebot.resources.markups

import dev.inmo.tgbotapi.types.buttons.KeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import me.y9san9.prizebot.actors.storage.giveaways_storage.GiveawaysStorage
import me.y9san9.prizebot.actors.storage.language_codes_storage.LanguageCodesStorage
import me.y9san9.prizebot.extensions.telegram.locale
import me.y9san9.telegram.updates.hierarchies.FromChatLocalizedDIBotUpdate


fun <T> mainMarkup(update: FromChatLocalizedDIBotUpdate<T>): KeyboardMarkup where
        T : GiveawaysStorage, T : LanguageCodesStorage {
    val storage = update.di
    val userId = update.chatId
    val locale = update.locale

    val buttons = listOf (
        listOf (
            SimpleKeyboardButton(locale.helpKeyboard),
            SimpleKeyboardButton(locale.giveawayKeyboard)
        )
    )

    val giveawaysButton = listOf(SimpleKeyboardButton(locale.selfGiveawaysKeyboard))
    val hasSelfGiveawaysButton = storage.getUserGiveaways(userId).isNotEmpty()

    return ReplyKeyboardMarkup (
        resizeKeyboard = true,
        keyboard = if(hasSelfGiveawaysButton) buttons + listOf(giveawaysButton) else buttons
    )
}
