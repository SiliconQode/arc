grammar DetStrat;

detection_strategy : STRATEGY annot identifier LEFT_BRACE filter* RIGHT_BRACE;

filter 
  : membership_filter
  | relation_filter
  | marginal_filter
  | composite_filter
  | interval_filter
  | cluster_filter
  | historical_filter
  | architectural_filter
  | validation_filter
  | lexical_filter
  | disharmony_filter
  | behavioral_filter
  | evolution_filter
  | structural_filter
  ;
  
membership_filter : MEMBERSHIP;

relation_filter : metric_expr op=relation_op value_expr;

marginal_filter
  : relative_filter
  | absolute_filter
  | statistical_filter
  ;
  
composite_filter : op=composite_op OF LEFT_BRACE filter_list RIGHT_BRACE;

filter_list : filter (connective NOT? filter)*;
  
relative_filter : op=relative_op value_or_percent VALUES FOR metric;

absolute_filter : op=absolute_op THAN value FOR metric;

statistical_filter : STAT;

interval_filter : lb=marginal_filter lbop=op metric ubop=op ub=marginal_filter;

cluster_filter : CLUSTER;

historical_filter : HISTORY;

architectural_filter : ARCH;

validation_filter : VALIDATE;

lexical_filter : NAME lexical_op string;

disharmony_filter : CONTAINS op=containment_op value identifier;

behavioral_filter : BEHAVIORAL;

evolution_filter : EVOLUTION;

structural_filter : STRUCTURAL;

metric_expr
  : identifier
  | identifier op=math_op identifier
  | LEFT_PARAM metric_expr RIGHT_PARAM
  ;
  
value_expr
  : relative_value
  | fractional_value
  | statistical_value
  ;
  
relative_value
  : FEW
  | MANY
  | SHORT_TERM
  | VERY_LOW
  | LOW
  | NOMINAL
  | HIGH
  | VERY_HIGH
  ;

statistical_value : statistical_op LEFT_PARAM metric_expr RIGHT_PARAM;

connective
  : AND
  | OR
  | BUT
  ;
  
containment_op
  : MORE_THAN
  | LESS_THAN
  | AT_LEAST
  | AT_MOST
  | EXACTLY
  ;

composite_op
  : intersection_of
  | union_of
  | xor_between
  ;
  
relative_op
  : TOP
  | BOTTOM
  ;

lexical_op
  : BEGINS_WITH
  | ENDS_WITH
  | CONTAINS
  | IS_LIKE
  ;
  
statistical_value
  : AVERAGE
  | MEAN
  | MEDIAN
  | MAX
  | MIN
  | STDDEV
  ;
  
value_or_percent
  : int
  | percent
  ;

// Keywords

STRATEGY : 'strategy';
MEMBERSHIP : 'member';
RELATION : 'relation';
RELATIVE : 'relative';
ABSOLUTE : 'absolute';
STAT : 'stat';
COMPOSITE : 'composite';
INTERVAL : 'interval';
CLUSTER : 'cluster';
HISTORY : 'history';
ARCH : 'arch';
VALIDATE : 'validate';
LEXICAL : 'lexical';
DISHARMONY : 'disharmony';
BEHAVIORAL : 'behavioral';
EVOLUTION : 'evolution';
STRUCTURAL : 'structural';


// Composite Operators

UNION : 'union';
INTERSECTION : 'intersection';

// Lexical Operators

BEGINS_WITH : 'begins_with';
ENDS_WITH : 'ends_with';
CONTAINS : 'contains';
IS_LIKE : 'is_like';

// Connective Operators

AND : 'and';
OR : 'or';
XOR : 'xor';
BUT : 'but';
DIFF : 'excluding';

// Disharmony and Structural Operators

IS_A : 'IS-A';
HAS_A : 'HAS-A';

// Relational Operators

LT  : '<';
LTE : '<=';
GT  : '>';
GTE : '>=';
EQ  : '==';
NE  : '!=';

// Statistical Operators

MEAN : 'MEAN';
MIN  : 'MIN';
MAX  : 'MAX';
MEDIAN : 'MEDIAN';
STDDEV : 'STDDEV';

// Relative Operators

TOP : 'top';
BOTTOM : 'bottom';

// Separators

LEFT_BRACE : '{';
RIGHT_BRACE : '}';
VALUES : 'values';
FOR : 'for';
BETWEEN : 'between';
OF : 'of';

// Whitespace and Comments

SL_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN);
ML_COMMENT : '/*' .*? '*/' -> channel(HIDDEN);
WS : [ \t\r\n\u000C]+ -> skip;
