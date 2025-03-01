package com.yourssu.design.compose.atom

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourssu.design.R
import com.yourssu.design.compose.YdsTheme
import com.yourssu.design.compose.base.NoRippleButton
import com.yourssu.design.compose.foundation.IconSize
import com.yourssu.design.compose.foundation.YdsIcon
import com.yourssu.design.compose.states.ButtonColorState
import com.yourssu.design.compose.states.ButtonSizeState

enum class PlainButtonSize {
    Large, Medium, Small
}

@Composable
private fun plainButtonSizeStateBySize(
    size: PlainButtonSize
): ButtonSizeState = when (size) {
    PlainButtonSize.Large -> ButtonSizeState(iconSize = IconSize.Medium)
    PlainButtonSize.Medium -> ButtonSizeState(
        typo = YdsTheme.typography.button3,
        iconSize = IconSize.Small
    )
    PlainButtonSize.Small -> ButtonSizeState(
        typo = YdsTheme.typography.button4,
        iconSize = IconSize.ExtraSmall
    )
}

@Composable
private fun plainButtonColor(
    isWarned: Boolean,
    isPointed: Boolean
) = ButtonColorState(
    contentColor = when {
        isWarned -> YdsTheme.colors.buttonWarned
        isPointed -> YdsTheme.colors.buttonPoint
        else -> YdsTheme.colors.buttonNormal
    },
    disabledContentColor = YdsTheme.colors.buttonDisabled
)

@Composable
fun PlainButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "",
    @DrawableRes leftIcon: Int? = null,
    @DrawableRes rightIcon: Int? = null,
    isDisabled: Boolean = false,
    isWarned: Boolean = false,
    isPointed: Boolean = false,
    sizeType: PlainButtonSize = PlainButtonSize.Medium,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val sizeState = plainButtonSizeStateBySize(size = sizeType)
    val typo = sizeState.typo
    val iconSize = sizeState.iconSize

    NoRippleButton(
        onClick = onClick,
        modifier = modifier,
        enabled = !isDisabled,
        colors = plainButtonColor(isWarned = isWarned, isPointed = isPointed),
        elevation = elevation(0.dp, 0.dp, 0.dp),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (sizeType == PlainButtonSize.Large) {
            // sizeType이 Large일 때는 아이콘만
            val iconRes = leftIcon ?: rightIcon

            require(iconRes != null) {
                "Large 버튼은 아이콘이 지정되어야 합니다."
            }
            YdsIcon(
                id = iconRes,
                iconSize = iconSize
            )
        } else {
            leftIcon?.let { icon ->
                YdsIcon(
                    id = icon,
                    iconSize = iconSize
                )
                Spacer(modifier = Modifier.width(2.dp))
            }

            Text(
                text = text,
                style = typo
            )

            if (leftIcon == null) {
                rightIcon?.let { icon ->
                    Spacer(modifier = Modifier.width(2.dp))
                    YdsIcon(
                        id = icon,
                        iconSize = iconSize
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlainButtonPreview() {

    var sizeState by rememberSaveable { mutableStateOf(PlainButtonSize.Large) }
    var pointed by remember { mutableStateOf(false) }

    YdsTheme {
        PlainButton(
            onClick = {
                sizeState = PlainButtonSize.Medium
                pointed = true
            },
            leftIcon = R.drawable.ic_ground_filled,
            sizeType = sizeState,
            text = "Large",
            isPointed = pointed
        )
    }
}