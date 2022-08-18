grammar ArcFlow;

workflow : WORKFLOW ID LEFT_BRACE phase* END PHASE;

phase : PHASE ID command* END PHASE;

command
    : tool_command
    | build_command
    | repo_command
    | primary_analysis
    | secondary_analysis
    | artifact_identifier
    | script
    ;

tool_command : TOOL ID;
build_command : BUILD ID;
script : ;

// Keywords

WORKFLOW : 'workflow';
PHASE : 'phase';
END : 'end';
TOOL : 'tool';
BUILD : 'build';

// Separators

ID : [a-zA-Z_][a-zA-Z0-9_]*;
LEFT_BRACE : '{';
RIGHT_BRACE : '}';