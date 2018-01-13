package de.turbocache3000.polizei.log.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StringFormatterTest {
    @Test
    fun `no parameters`() {
        val formatted = StringFormatter.format("test")

        assertThat(formatted).isEqualTo("test")
    }

    @Test
    fun `1 parameter`() {
        val formatted = StringFormatter.format("test {}", "foo")

        assertThat(formatted).isEqualTo("test foo")
    }

    @Test
    fun `2 parameter`() {
        val formatted = StringFormatter.format("test {} {}", "foo", "bar")

        assertThat(formatted).isEqualTo("test foo bar")
    }

    @Test
    fun `2 placeholder, 1 parameter`() {
        val formatted = StringFormatter.format("test {} {}", "foo")

        assertThat(formatted).isEqualTo("test foo {}")
    }
}