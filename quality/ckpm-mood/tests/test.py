import ast
from ckpm.ck_metrics import CKMetrics
from ckpm.mood_metrics import MOODMetrics
from ckpm.other_metrics import OtherMetrics
import ckpm.raw as raw
from ckpm.data_model import Project
from ckpm.visitor import Visitor


def set_loc(item, raw_metrics):
    item.metrics['loc'] = raw_metrics.loc
    item.metrics['sloc'] = raw_metrics.sloc
    item.metrics['lloc'] = raw_metrics.lloc
    item.metrics['bloc'] = raw_metrics.blank
    item.metrics['mcloc'] = raw_metrics.multi
    item.metrics['scloc'] = raw_metrics.single_comments
    item.metrics['cp'] = raw_metrics.comments / (raw_metrics.loc - raw_metrics.blank - raw_metrics.comments)


def evaluate_metrics(item, proj):
    metrics = CKMetrics(proj)
    metrics.evaluate(item)

    metrics = MOODMetrics(proj)
    metrics.evaluate(item)

    metrics = OtherMetrics(proj)
    metrics.evaluate(item)


if __name__ == '__main__':
    mdef = '''
import re    

class Test(Base):
  def __init__():
    self.x = 0
    self.y = 1

  def method_1():
    self.x = 0
    if self.x == 1:
      print("test method 1")
    elif self.x == 2:
      print("other")
    else:
      self._method_2()

  def _method_2():
    self.x = re.Match()

class Test2(Test):
  def __init__():
    self.test = Test()
    
bob = ""

def use_bob():
  bob += "other"
    '''

    tree = ast.parse(mdef)

    project = Project('Test')
    visitor = Visitor(project, 'TestMod')
    visitor.visit(tree)
    mod = visitor.module
    loc = raw.analyze(mdef)
    set_loc(mod, loc)
    for clss in mod.classes:
        lines = mdef.split('\n')
        cls_def = '\n'.join(lines[clss.start - 1: clss.end])
        cls_raw = raw.analyze(cls_def)
        set_loc(clss, cls_raw)
        evaluate_metrics(clss, project)
    evaluate_metrics(mod, project)

    print(project)

    for m in project.modules:
        print(m.uses)
        print(m.vars)

    print(ast.dump(tree, indent=4))
