package me.y9san9.prizebot.handlers.private_messages.fsm.states.giveaway

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.y9san9.fsm.FSMStateResult
import me.y9san9.fsm.stateResult
import me.y9san9.prizebot.actors.giveaway.CreateGiveawayActor
import me.y9san9.prizebot.actors.telegram.sender.CancellationToMainStateSender
import me.y9san9.prizebot.extensions.telegram.*
import me.y9san9.prizebot.handlers.private_messages.fsm.states.MainState
import me.y9san9.prizebot.resources.locales.Locale
import me.y9san9.telegram.updates.extensions.send_message.sendMessage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset


@SerialName("timezone_input")
@Serializable
data class TimezoneInputData (
    val title: String,
    val participateText: String,
    val localDate: String
)

object TimezoneInputState : PrizebotFSMState<TimezoneInputData> {
    override suspend fun process (
        data: TimezoneInputData,
        event: PrizebotPrivateMessageUpdate
    ): FSMStateResult<*> {
        event.commandOrDefault {
            raw("/cancel") {
                return CancellationToMainStateSender.send(event)
            }
            raw(Locale::customTimezone) {
                return stateResult(CustomTimezoneInputState, data) {
                    event.sendMessage(event.locale.customTimezoneInput)
                }
            }
            raw(Locale::GMT) {
                return nextState(event, data, ZoneOffset.UTC)
            }
            raw(Locale::`UTC-4`) {
                return nextState(event, data, ZoneOffset.ofHours(-4))
            }
            raw(Locale::UTC1) {
                return nextState(event, data, ZoneOffset.ofHours(1))
            }
            raw(Locale::UTC2) {
                return nextState(event, data, ZoneOffset.ofHours(2))
            }
            raw(Locale::UTC3) {
                return nextState(event, data, ZoneOffset.ofHours(3))
            }
            raw(Locale::UTC5_30) {
                return nextState(event, data, ZoneOffset.ofHoursMinutes(5, 30))
            }
            raw(Locale::UTC8) {
                return nextState(event, data, ZoneOffset.ofHours(4))
            }
            raw(Locale::UTC9) {
                return nextState(event, data, ZoneOffset.ofHours(9))
            }
        }

        return stateResult(TimezoneInputState, data)
    }

    private suspend fun nextState (
        update: PrizebotPrivateMessageUpdate,
        data: TimezoneInputData,
        offset: ZoneOffset
    ) = CreateGiveawayActor.create (
        update, data.title, data.participateText,
        OffsetDateTime.of(LocalDateTime.parse(data.localDate, dateTimeFormatter), offset)
    )
}
