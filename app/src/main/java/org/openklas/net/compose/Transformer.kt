package org.openklas.net.compose

import io.reactivex.ObservableTransformer

/**
 * OpenKlas
 * Class: Transformer
 * Created by limmoong on 2020/11/18.
 *
 * Description:
 */

abstract class Transformer<T> : ObservableTransformer<T, T>