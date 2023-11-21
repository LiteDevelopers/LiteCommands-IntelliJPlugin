package dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation;

import com.intellij.codeInspection.ProblemHighlightType;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AttributeType;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapperSchema;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.ValidationAnnotationInspectionTool;

public class CommandValidationInspection extends ValidationAnnotationInspectionTool {

    private static final AnnotationMapperSchema COMMAND_VALIDATOR = AnnotationMapperSchema.builder()
            .name(Command.class.getName())
            .attribute("name", AttributeType.STRING, CommandNameValidUtils::validateMultiRoute, RouteQuickFix.multiple())
            .attribute("aliases", AttributeType.ARRAY, CommandNameValidUtils::validateMultiRoute, RouteQuickFix.multiple())
            .build();

    public CommandValidationInspection() {
        super(COMMAND_VALIDATOR, ProblemHighlightType.GENERIC_ERROR);
    }

}
