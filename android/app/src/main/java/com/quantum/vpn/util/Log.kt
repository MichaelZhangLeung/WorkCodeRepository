package com.quantum.vpn.util

import com.quantum.vpn.BuildConfig

object Log {

    private val LOG = if (BuildConfig.DEBUG) DebugLogImpl() else ReleaseLogImpl()

    fun v(tag: String, msg: String): Int {
        return LOG.v(tag, msg)
    }

    fun v(tag: String, msg: String, tr: Throwable): Int {
        return LOG.v(tag, msg, tr)
    }

    fun d(tag: String, msg: String): Int {
        return LOG.d(tag, msg)
    }

    fun d(tag: String, msg: String, tr: Throwable): Int {
        return LOG.d(tag, msg, tr)
    }

    fun i(tag: String, msg: String): Int {
        return LOG.i(tag, msg)
    }

    fun i(tag: String, msg: String, tr: Throwable): Int {
        return LOG.i(tag, msg, tr)
    }

    fun w(tag: String, msg: String): Int {
        return LOG.w(tag, msg)
    }

    fun w(tag: String, msg: String, tr: Throwable): Int {
        return LOG.w(tag, msg, tr)
    }

    fun e(tag: String, msg: String): Int {
        return LOG.e(tag, msg)
    }

    fun e(tag: String, msg: String, tr: Throwable): Int {
        return LOG.e(tag, msg, tr)
    }

    private interface ILog {
        fun d(tag: String, msg: String): Int

        fun d(tag: String, msg: String, tr: Throwable): Int

        fun i(tag: String, msg: String): Int

        fun i(tag: String, msg: String, tr: Throwable): Int

        fun v(tag: String, msg: String): Int

        fun v(tag: String, msg: String, tr: Throwable): Int

        fun w(tag: String, msg: String): Int

        fun w(tag: String, msg: String, tr: Throwable): Int

        fun e(tag: String, msg: String): Int

        fun e(tag: String, msg: String, tr: Throwable): Int
    }

    private class DebugLogImpl : ILog {


        override fun d(tag: String, msg: String): Int {
            return android.util.Log.d(tag, msg)
        }

        override fun d(tag: String, msg: String, tr: Throwable): Int {
            return android.util.Log.d(tag, msg, tr)
        }

        override fun i(tag: String, msg: String): Int {
            return android.util.Log.i(tag, msg)
        }

        override fun i(tag: String, msg: String, tr: Throwable): Int {
            return android.util.Log.i(tag, msg, tr)
        }

        override fun v(tag: String, msg: String): Int {
            return android.util.Log.v(tag, msg)
        }

        override fun v(tag: String, msg: String, tr: Throwable): Int {
            return android.util.Log.v(tag, msg, tr)
        }

        override fun w(tag: String, msg: String): Int {
            return android.util.Log.w(tag, msg)
        }

        override fun w(tag: String, msg: String, tr: Throwable): Int {
            return android.util.Log.w(tag, msg, tr)
        }

        override fun e(tag: String, msg: String): Int {
            return android.util.Log.e(tag, msg)
        }

        override fun e(tag: String, msg: String, tr: Throwable): Int {
            return android.util.Log.e(tag, msg, tr)
        }
    }

    private class ReleaseLogImpl : ILog {

        override fun d(tag: String, msg: String): Int {
            return 0
        }

        override fun d(tag: String, msg: String, tr: Throwable): Int {
            return 0
        }

        override fun i(tag: String, msg: String): Int {
            return 0
        }

        override fun i(tag: String, msg: String, tr: Throwable): Int {
            return 0
        }

        override fun v(tag: String, msg: String): Int {
            return 0
        }

        override fun v(tag: String, msg: String, tr: Throwable): Int {
            return 0
        }

        override fun w(tag: String, msg: String): Int {
            return 0
        }

        override fun w(tag: String, msg: String, tr: Throwable): Int {
            return 0
        }

        override fun e(tag: String, msg: String): Int {
            return 0
        }

        override fun e(tag: String, msg: String, tr: Throwable): Int {
            return 0
        }
    }
}
