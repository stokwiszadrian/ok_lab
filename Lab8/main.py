class Node:
    def __init__(self, label, duration, prev=None, nextnode=None):
        self.label = label
        self.duration = duration
        self.prev = prev
        self.next = nextnode
        self.estart = None
        self.efinish = None
        self.lstart = None
        self.lfinish = None
        self.float = None
        self.level = 0


class Tasklist:
    def __init__(self, nodelist):
        self.nodelist = nodelist
        self.efinish = 0
        starting_nodes = list(filter(lambda x: x.prev is None, self.nodelist))
        for node in starting_nodes:
            self.travel(node, 0)
        self.nodelist.sort(key=lambda x: x.level)

    def __str__(self):
        s = ""
        for node in self.nodelist:
            s = s + f"""
{node.label}:  
(
    duration: {node.duration},
    prev: {node.prev},
    next: {node.next},
    level: {node.level},
    estart: {node.estart},
    efinish: {node.efinish},
    lstart: {node.lstart},
    lfinish: {node.lfinish},
    float: {node.float}
)
            """
        return s

    def travel(self, curnode, level):
        if curnode.label == "G":
            print("HERE", curnode.level, level)
        curindex = self.nodelist.index(curnode)
        if curnode.level is None or curnode.level < level:
            curnode.level = level
        if curnode.label == "G":
            print("next lvl", curnode.level)
        self.nodelist[curindex] = curnode
        if curnode.label == "G":
            print(self.nodelist[curindex].level)
        if curnode.next is not None:
            for node in list(filter(lambda x: x.label in curnode.next, self.nodelist)):
                self.travel(node, level + 1)

    def forward_pass(self):
        for node in self.nodelist:
            if node.prev is None:
                node.estart = 0
            elif len(node.prev) == 1:
                node.estart = list(filter(lambda x: x.label in node.prev, self.nodelist))[0].efinish
            elif len(node.prev) > 1:
                node.estart = max(
                                map(
                                    lambda x: x.efinish,
                                    filter(
                                        lambda x: x.label in node.prev,
                                        self.nodelist
                                    )))
            print(node.label)
            node.efinish = node.estart + node.duration
            if node.efinish > self.efinish:
                self.efinish = node.efinish
            # print(node.label, node.estart, node.efinish)
        # print(self.efinish)

    def backward_pass(self):
        self.nodelist.sort(key=lambda x: x.level, reverse=True)
        for node in self.nodelist:
            if node.next is None:
                node.lfinish = self.efinish
            elif len(node.next) == 1:
                node.lfinish = list(filter(lambda x: x.label in node.next, self.nodelist))[0].lstart
            elif len(node.next) > 1:
                node.lfinish = min(
                    map(
                        lambda x: x.lstart,
                        filter(
                            lambda x: x.label in node.next,
                            self.nodelist
                        )))
            node.lstart = node.lfinish - node.duration
            node.float = node.lfinish - node.efinish

    def cpm(self):
        self.forward_pass()
        self.backward_pass()
        self.nodelist.sort(key=lambda x: x.level)
        critical_path = []
        for node in self.nodelist:
            if node.float == 0:
                critical_path.append(node)
        return critical_path


if __name__ == '__main__':
    # nodes = [
    #     Node("A", 6, nextnode=["C"]),
    #     Node("B", 4, nextnode=["D", "E"]),
    #     Node("C", 3, ["A"], ["H"]),
    #     Node("D", 4, ["B"], ["H"]),
    #     Node("E", 3, ["B"], ["G"]),
    #     Node("F", 10, nextnode=["G"]),
    #     Node("G", 3, ["E", "F"]),
    #     Node("H", 2, ["C", "D"])
    # ]
    nodes = [
        Node("A", 10, nextnode=["B", "F", "H"]),
        Node("B", 20, ["A"], nextnode=["C"]),
        Node("C", 5, ["B"], ["D", "G"]),
        Node("D", 10, ["C"], ["E"]),
        Node("E", 20, ["D", "G", "H"]),
        Node("F", 15, ["A"],  nextnode=["G"]),
        Node("G", 5, ["C", "F"], ["E"]),
        Node("H", 15, ["A"], ["E"])
    ]
    tasks = Tasklist(nodes)
    print(tasks)
    cpm = tasks.cpm()
    print("START -> ", end="")
    for node in cpm:
        print(node.label, "-> ", end="")
    print("END", end="")
    # print(tasks)
    # import plotly.express as px
    # import pandas as pd
    #
    # df = pd.DataFrame([
    #     dict(Task="Job A", Start='2009-01-01', Finish='2009-02-28'),
    #     dict(Task="Job B", Start='2009-03-05', Finish='2009-04-15'),
    #     dict(Task="Job C", Start='2009-02-20', Finish='2009-05-30')
    # ])
    #
    # fig = px.timeline(df, x_start="Start", x_end="Finish", y="Task")
    # fig.update_yaxes(autorange="reversed")  # otherwise tasks are listed from the bottom up
    # fig.show()
