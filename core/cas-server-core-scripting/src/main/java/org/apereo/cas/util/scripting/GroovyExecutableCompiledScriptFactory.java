package org.apereo.cas.util.scripting;

import groovy.text.GStringTemplateEngine;
import lombok.val;
import org.springframework.core.io.Resource;
import java.io.File;
import java.util.Map;
import java.util.Optional;

/**
 * This is {@link GroovyExecutableCompiledScriptFactory}.
 *
 * @author Misagh Moayyed
 * @since 7.1.0
 */
public class GroovyExecutableCompiledScriptFactory implements ExecutableCompiledScriptFactory {
    @Override
    public ExecutableCompiledScript fromResource(final Resource resource, final boolean watchResource) {
        return new WatchableGroovyScriptResource(resource);
    }

    @Override
    public String createTemplate(final String contents, final Map<String, ?> templateParams) throws Exception {
        val engine = new GStringTemplateEngine();
        val template = engine.createTemplate(contents).make(templateParams);
        return template.toString();
    }

    @Override
    public String createTemplate(final File templateFile, final Map<String, ?> templateParams) throws Exception {
        val engine = new GStringTemplateEngine();
        val template = engine.createTemplate(templateFile).make(templateParams);
        return template.toString();
    }

    @Override
    public Optional<String> getInlineScript(final String input) {
        val matcherInline = ScriptingUtils.getMatcherForInlineGroovyScript(input);
        return matcherInline.find() ? Optional.of(matcherInline.group(1)) : Optional.empty();
    }

    @Override
    public Optional<String> getExternalScript(final String input) {
        val matcherFile = ScriptingUtils.getMatcherForExternalGroovyScript(input);
        return matcherFile.find() ? Optional.of(matcherFile.group()) : Optional.empty();
    }

    @Override
    public ExecutableCompiledScript fromScript(final String script) {
        return new GroovyShellScript(script);
    }
}
