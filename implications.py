import sys

def read_formulas(file_name):
    with open(file_name, 'r') as file:
        return [line.strip() for line in file.readlines() if line.strip()]

def extract_variables(formula):
    return set(char for char in formula if char.isalpha() and char.isupper())

def generate_environments(variables):
    num_vars = len(variables)
    num_environments = 2 ** num_vars
    environments = []
    for i in range(num_environments):
        environment = {}
        for j, var in enumerate(variables):
            environment[var] = bool((i >> j) & 1)
        environments.append(environment)
    return environments

def evaluate_formula(formula, environment):
    return eval(formula, environment)

def left_implies_right(left, right, variables):
    for values in product([True, False], repeat=len(variables)):
        environment = dict(zip(variables, values))
        if evaluate_formula(left, environment) and not evaluate_formula(right, environment):
            return False
    return True

def right_implies_left(left, right, variables):
    for values in product([True, False], repeat=len(variables)):
        environment = dict(zip(variables, values))
        if evaluate_formula(right, environment) and not evaluate_formula(left, environment):
            return False
    return True

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python implications.py <filename>")
        sys.exit(1)

    file_name = sys.argv[1]
    formulas = read_formulas(file_name)
    if len(formulas) != 2:
        print("The file must contain exactly two logical expressions.")
        sys.exit(1)
    
    left = formulas[0]
    right = formulas[1]

    left_variables = extract_variables(left)
    right_variables = extract_variables(right)
    all_variables = left_variables.union(right_variables)
    
    if left_implies_right(left, right, all_variables) and right_implies_left(left, right, all_variables):
        print("EQUIVALENT")
    elif left_implies_right(left, right, all_variables):
        print("LEFT implies RIGHT")
    elif right_implies_left(left, right, all_variables):
        print("RIGHT implies LEFT")
    else:
        print("NO IMPLICATION")
