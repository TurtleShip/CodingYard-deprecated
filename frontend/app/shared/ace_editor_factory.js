'use strict';

(function () {
    app.factory('AceEditor', function AceEditorFactory() {
        var aceEditor = {};

        aceEditor.getMode = function (language) {
            switch (language) {
                case "JAVA":
                    return "java";
                case "C":
                    return "c_cpp";
                case "CPP":
                    return "c_cpp";
                case "PYTHON":
                    return "python";
                case "RUBY":
                    return "ruby";
                case "OTHER":
                    return "text";
                default:
                    return "text";
            }
        };

        aceEditor.getSetting = function () {
            return {
                mode: "text",
                useWrapMode: true,
                showGutter: true,
                theme: 'solarized_light'
            }
        };

        return aceEditor;
    });
})();