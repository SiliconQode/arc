/*
 * A grammar for the Role-Based Metamodeling Language written in ANTLR v4.
 */
grammar Rbml:

pattern : PATTERN ID LEFT_BRACE pattern_member* RIGHT_BRACE;

pattern_member
  : sps
//  | ips
  ;

sps : SPS LEFT_BRACE sps_member* RIGHT_BRACE;

sps_member
  : roles
  | constraints
  | relations
  ;
  
roles : ROLES LEFT_BRACE role* RIGHT_BRACE;

role
  : gen_hierarchy
  | classifier_role
  | class_role
  | interface_role
  | enum_role
  | data_type_role
  ;
  
gen_hierarchy : GEN_HIERARCHY ID LEFT_BRACE root_classifier gh_roles* LEFT_BRACE;

classifier_role : CLASSIFIER ID multiplicity? LEFT_BRACE features* RIGHT_BRACE;

class_role : CLASS ID multiplicity? LEFT_BRACE features* RIGHT_BRACE;

interface_role : INTERFACE ID multiplicity? LEFT_BRACE features* RIGHT_BRACE;

enum_role : ENUM ID multiplicity? LEFT_BRACE features* RIGHT_BRACE;

data_type_role : DATA_TYPE ID multiplicity? LEFT_BRACE features* RIGHT_BRACE;

multiplicity
  : LEFT_BRACKET digit+ DOTS (digit+ | STAR) RIGHT_BRACKET
  | LEFT_BRACKET (digit+ | STAR) RIGHT_BRACKET
  ;
  
features
  : behavioral_feature
  | structural_feature
  ;
  
behavioral_feature : BEH_FEATURE accessibility? modifier* ID LEFT_PARAM param_list? RIGHT_PARAM (COLON type=ID)? multiplicity?;

param_list : param (COMMA param)*;

param : ID (COLON type=ID)?;

structural_feature : STR_FEATURE accessibility? modifier* ID multiplicity? (COLON type=ID)?;

accessibility : PLUS | MINUS | HASH | TILDE;

modifier
  : ABSTRACT
  | STATIC
  ;

relations : RELATIONS LEFT_BRACE relation* RIGHT_BRACE;

relation
  : association_role
  | aggregation_role
  | composition_role
  | generalization_role
  | realization_role
  | usage_role
  | dependency_role
  ;
  
association_role : ASSOCIATION ID multiplicity? ARROW (end_point COMMA end_point);

aggregation_role : AGGREGATION ID multiplicity? ARROW (end_point COMMA end_point);

composition_role : COMPOSITION ID multiplicity? ARROW (end_point COMMA end_point);

generalization_role : GENERALIZATION ID multiplicity? ARROW (source=rel_end COMMA dest=rel_end);

realization_role : REALIZATION ID multiplicity? ARROW (source=rel_end COMMA dest=rel_end);

usage_role : USAGE ID multiplicity? ARROW (source=rel_end COMMA dest=rel_end);

dependency_role : DEPENDENCY ID multiplicity? ARROW (source=rel_end COMMA dest=rel_end);

end_point : ID (COLON type=ID)? multiplicity?

rel_end : ID multiplicity?

ID : [a-zA-Z_][a-zA-Z0-9_]*;

// Keywords

PATTERN : 'pattern';
SPS : 'sps';
ROLES : 'roles';
GEN_HIERARCHY : 'genhierarchy';
CLASSIFIER : 'classifier';
CLASS : 'class';
INTERFACE : 'interface';
ENUM : 'enum';
DATA_TYPE : 'datatype';
BEH_FEATURE : 'behave';
STR_FEATURE : 'struct';
ASSOCIATION : 'assoc';
AGGREGATION : 'aggr';
COMPOSITION : 'comp';
GENERALIZATION : 'gen';
REALIZATION : 'real';
USAGE : 'use';
DEPENDENCY : 'dep';

// Modifiers

ABSTRACT : 'abstract';
STATIC : 'static';

// Accessibility Operators

PLUS : '+';
MINUS : '-';
HASH : '#';
TILDE : '~';

// Separators

LEFT_BRACE : '{';
RIGHT_BRACE : '}';
LEFT_BRACKET : '[';
RIGHT_BRACKET : ']';
LEFT_PARAM : '(';
RIGHT_PARAM : ')';
DOTS : '..';
COLON : ':';
ARROW : '->';

// Whitespace and Comments

SL_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN);
ML_COMMENT : '/*' .*? '*/' -> channel(HIDDEN);
WS : [ \t\r\n\u000C]+ -> skip;
