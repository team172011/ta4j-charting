/*
 GNU Lesser General Public License

 Copyright (c) 2017 Wimmer, Simon-Justus

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.sjwimmer.tacharting.chart.model.types;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.*;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.indicators.bollinger.*;
import org.ta4j.core.indicators.candles.LowerShadowIndicator;
import org.ta4j.core.indicators.candles.RealBodyIndicator;
import org.ta4j.core.indicators.candles.UpperShadowIndicator;
import org.ta4j.core.indicators.helpers.*;
import org.ta4j.core.indicators.keltner.KeltnerChannelLowerIndicator;
import org.ta4j.core.indicators.keltner.KeltnerChannelMiddleIndicator;
import org.ta4j.core.indicators.keltner.KeltnerChannelUpperIndicator;
import org.ta4j.core.indicators.volume.*;

/**
 * (In progress..)
 * Enum of all available indicators with names for gui.
 * @apiNote this enumeration lists only "real" ta4j indicators,
 * that means indicator that have a ta4j implementation like BollingeBandsMiddleIndicator
 *
 */
public enum IndicatorType {
    AROON_UP("Arron Up", AroonUpIndicator.class),
    AROON_DOWN("Arron Down", AroonDownIndicator.class),
    AVERAGE_DIRECTIONAL_MOVEMENT("Average Directional Movement/ADX", ADXIndicator.class),
    BOLLINGER_BANDS_MIDDLE("Bollinger Bands Middle", BollingerBandsMiddleIndicator.class),
    BOLLINGER_BANDS_UPPER("Bollinger Bands Upper", BollingerBandsUpperIndicator.class),
    BOLLINGER_BANDS_LOWER("Bollinger Bands Lower", BollingerBandsLowerIndicator.class),
    BOLLINGER_BANDS_WIDTH("Bollinger Bands Width", BollingerBandWidthIndicator.class),
    CMO("CMO", CMOIndicator.class),
    CLOSE("Close Price", ClosePriceIndicator.class),
    EMA("Exponential Moving Average", EMAIndicator.class),
    KAMA("KAMA", KAMAIndicator.class),
    KELTNER_MIDDLE("Keltener Middle Chanel", KeltnerChannelMiddleIndicator.class),
    KELTNER_Upper("Keltener Uper Chanel", KeltnerChannelUpperIndicator.class),
    KELTNER_LOWER("Keltener Lower Chanel", KeltnerChannelLowerIndicator.class),
    LOWER_SHADOW("Lower Shadow", LowerShadowIndicator.class),
    MACD("MACD", MACDIndicator.class),
    MAX("High Price", HighPriceIndicator.class),
    MIN("Low Price", LowPriceIndicator.class),
    MVWAP("MVWAP", MVWAPIndicator.class),
    NVI("NVI", NVIIndicator.class),

    ON_BALANCE_VOLUMEN("On Balance Volumen", OnBalanceVolumeIndicator.class),
    OPEN("Open Price", OpenPriceIndicator.class),

    PERCENT_BI("Percent BI", PercentBIndicator.class),
    PREVIOUS_VALUE("Previous Value", PreviousValueIndicator.class),
    PVI("PVI", PVIIndicator.class),




    REAL_BODY("Real Body", RealBodyIndicator.class),
    SMOOTHED_RSI("Smoothed Relaive Strength", RSIIndicator.class),
    SMA("Simple Moving Average", SMAIndicator.class),
    STOCHASTIC_RSI("Stochastic RSI", StochasticRSIIndicator.class),
    STOCHASTIC_OscillatorK("Stochastic Oscillator K", StochasticRSIIndicator.class),
    TRIPLE_EMA("Triple EMA", TripleEMAIndicator.class),
    TRUE_RANGE("True Range", TRIndicator.class),
    ULCER_INDEX("Ulcer Index", UlcerIndexIndicator.class),
    UPPER_SHADOW("Upper Shadow", UpperShadowIndicator.class),
    VWAP("VWAP", VWAPIndicator.class),
    WMA("WMA", WMAIndicator.class)
    ;




    private final String name;
    private final Class<? extends Indicator> clazz;

    IndicatorType(String name, Class<? extends Indicator> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public Class<? extends Indicator> getClazz(){
        return this.clazz;
    }

    public static Class<? extends Indicator> valueOf(Indicator indicator){
        for(IndicatorType n: IndicatorType.values()){
            if (indicator.getClass() == n.getClazz()){
                System.out.println(n.clazz);
                return n.getClazz();
            }
        }

        return null;
    }
}
